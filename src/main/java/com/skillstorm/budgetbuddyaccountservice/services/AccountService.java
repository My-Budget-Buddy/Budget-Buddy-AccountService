package com.skillstorm.budgetbuddyaccountservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.exceptions.AccountNotFoundException;
import com.skillstorm.budgetbuddyaccountservice.exceptions.IdMismatchException;
import com.skillstorm.budgetbuddyaccountservice.exceptions.NotEnoughInformationException;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Transaction;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;

@Service
public class AccountService {

    private final LoadBalancerClient loadBalancerClient;
    private final RestClient restClient;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(LoadBalancerClient loadBalancerClient, AccountRepository accountRepository, AccountMapper accountMapper) {
        this.loadBalancerClient = loadBalancerClient;
        this.restClient = RestClient.builder()
                .build();
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    // Get Accounts by userId
    public List<AccountDto> getAccountsByUserId(String userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        List<AccountDto> accountDtos = new ArrayList<>(accounts.size());

        List<Transaction> transactions = getTransactionsByUserId(userId);

        for (Account account : accounts) {

            List<Transaction> accountTransactions = transactions.stream()
                    .filter(transaction -> transaction.getAccountId() == (account.getId()))
                    .collect(Collectors.toList());

            BigDecimal currentBalance = calculateCurrentBalance(account.getStartingBalance(), accountTransactions);

            AccountDto accountDto = accountMapper.toDto(account);
            accountDto.setCurrentBalance(currentBalance);

            accountDto.setAccountNumber(maskAccountNumber(account.getAccountNumber()));
            accountDto.setRoutingNumber(maskRoutingNumber(account.getRoutingNumber()));

            accountDtos.add(accountDto);
        }
        return accountDtos;
    }

    // Get a list of transfers by userId from transaction microservice
    public List<Transaction> getTransactionsByUserId(String userId) {
        try {
            ServiceInstance instance = loadBalancerClient.choose("transaction-service");
            if (instance != null) {
                String serviceUrl = instance.getUri().toString();
                String fullUrl = serviceUrl + "/transactionsPrivate/user/" + userId;

                return restClient.get()
                        .uri(fullUrl)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<Transaction>>() {});
            } else {
                throw new IllegalStateException("No instances available for transaction_service");
            }
        } catch (HttpClientErrorException e) {
            return new ArrayList<>(0);
        }
    }

    // Get Accounts by accountId and userId
    public Optional<AccountDto> getAccountByAccountIdAndUserId(String userId, int id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent() && accountOptional.get().getUserId().equals(userId)) {
            Account account = accountOptional.get();
            List<Transaction> allTransactions = getTransactionsByUserId(userId);
            List<Transaction> accountTransactions = filterTransactionsByAccountId(allTransactions, account.getId());

            BigDecimal currentBalance = calculateCurrentBalance(account.getStartingBalance(), accountTransactions);

            AccountDto accountDto = accountMapper.toDto(account);
            accountDto.setCurrentBalance(currentBalance);

            accountDto.setAccountNumber(maskAccountNumber(account.getAccountNumber()));
            accountDto.setRoutingNumber(maskRoutingNumber(account.getRoutingNumber()));

            return Optional.of(accountDto);
        }
        return Optional.empty();
    }

    // Filter transfers of a user for a specific account
    private List<Transaction> filterTransactionsByAccountId(List<Transaction> transactions, int accountId) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccountId() == accountId)
                .collect(Collectors.toList());
    }

    // Create an account based on userId
    public AccountDto createAccount(Account account, String userId) {
        if (account.getAccountNumber() == null || account.getInstitution() == null || account.getType() == null) {
            List<String> errors = new ArrayList<>();
            if (account.getAccountNumber() == null) {
                errors.add("account number");
            }
            if (account.getInstitution() == null) {
                errors.add("institution");
            }
            if (account.getType() == null) {
                errors.add("type");
            }

            String errorString;
            if (errors.size() > 1) {
                errorString = "No " + String.join(", ", errors.subList(0, errors.size() - 1)) + " or "
                        + errors.get(errors.size() - 1);
            } else {
                errorString = "No " + errors.get(0);
            }

            throw new NotEnoughInformationException(errorString);
        }

        // Make sure starting balance is never null
        if (account.getStartingBalance() == null) {
            account.setStartingBalance(new BigDecimal(0));
        }

        account.setId(0); // Make sure the user cannot change an existing account on accident
        account.setUserId(userId);
        Account newAccount = accountRepository.save(account);
        return getAccountByAccountIdAndUserId(userId, newAccount.getId()).get();
    }

    // Update an account based on userId
    public int updateAccount(int id, String userId, Account.AccountType type, String accountNumber,
            String routingNumber, String institution, BigDecimal investmentRate,
            BigDecimal startingBalance) {

        // will throw 404 if acc doesn't exist, or 403 if account doesn't belong to this user
        verifyAccountOwnership(userId, id);
        
        return accountRepository.updateAccount(id, userId, type, accountNumber, routingNumber, institution,
                investmentRate, startingBalance);
    }

    // Delete Account based on userId
    public void deleteAccount(int id, String userId) {
        // will throw 404 if acc doesn't exist, or 403 if account doesn't belong to this user
        verifyAccountOwnership(userId, id);
        
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(a -> {
            if (a.getUserId().equals(userId)) {
                accountRepository.deleteById(id);
            }
        });
    }

    // Delete Account based on userId
    public void deleteAllAccounts(String userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        for (Account account : accounts) {
            accountRepository.deleteById(account.getId());
        }
    }

    // Since we are removing the need for a user id in the URL path, we can just
    // fetch the data for the user from the header
    // this is no less secure than comparing the header with the given id from the
    // url path
    public void validateRequestWithHeaders(HttpHeaders httpHeaders) {
        String headerUserId = httpHeaders.getFirst("User-ID");
        if (headerUserId == null) {
            throw new IdMismatchException();
        }
    }

    // Calculate the current balance of each account
    private BigDecimal calculateCurrentBalance(BigDecimal startingBalance, List<Transaction> transactions) {
        BigDecimal balance = startingBalance;

        for (Transaction transaction : transactions) {
            if ("Income".equals(transaction.getCategory())) {
                balance = balance.add(BigDecimal.valueOf(transaction.getAmount()));
            } else {
                balance = balance.subtract(BigDecimal.valueOf(transaction.getAmount()));
            }
        }

        return balance;
    }

    // Mask account number
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return accountNumber;
        }
        int length = accountNumber.length();
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            masked.append('*');
        }
        masked.append(accountNumber.substring(length - 4));
        return masked.toString();
    }

    // Mask routing number
    private String maskRoutingNumber(String routingNumber) {
        if (routingNumber == null || routingNumber.length() <= 4) {
            return routingNumber;
        }

        int length = routingNumber.length();
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            masked.append('*');
        }
        masked.append(routingNumber.substring(length - 4));
        return masked.toString();
    }

    /**
     * Verify that the account exists, and belongs to the requesting user. Throws 404 if account
     * doesn't exist, or 403 if the account doesn't belong to the user, or cleanly exits if ok.
     * @param requesterId The user making the request, obtained from `USER-ID` header
     * @param accountId The account being modified/deleted
     */
    private void verifyAccountOwnership(String requesterId, int accountId) {
        // add a check to make sure the account belongs to this user
        Optional<Account> existingAccount = accountRepository.findById(accountId);

        // first check if the account exists
        if (!existingAccount.isPresent()) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }

        // check if account userId matches given userId
        if (!existingAccount.get().getUserId().equals(requesterId)) {
            throw new IdMismatchException();
        }
    }
}
