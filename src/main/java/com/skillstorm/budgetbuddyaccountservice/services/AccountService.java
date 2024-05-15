package com.skillstorm.budgetbuddyaccountservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.budgetbuddyaccountservice.exceptions.NotEnoughInformationException;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Get Accounts by userId
    public List<Account> getAccountsByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    // Get Accounts by accountId and userId
    public Optional<Account> getAccountByAccountIdAndUserId(String userId, int id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent() && account.get().getUserId().equals(userId)) {
            return account;
        }
        return Optional.empty();
    }

    // Create an account based on userId
    public Account createAccount(Account account, String userId) {
        if (account.getAccountNumber() == null || account.getInstitution() == null || account.getRoutingNumber() == null
                || account.getType() == null) {
            List<String> errors = new ArrayList<>();
            if (account.getAccountNumber() == null) {
                errors.add("account number");
            }
            if (account.getInstitution() == null) {
                errors.add("institution");
            }
            if (account.getRoutingNumber() == null) {
                errors.add("routing number");
            }
            if (account.getType() == null) {
                errors.add("type");
            }

            String errorString;
            if (errors.size() > 1) {
                errorString = "No " + String.join(", ", errors.subList(0, errors.size() - 1)) + " or " + errors.get(errors.size() - 1);
            } else {
                errorString = "No " + errors.get(0);
            }

            throw new NotEnoughInformationException(errorString);
        }
        account.setId(0); // Make sure the user cannot change an existing account on accident
        account.setUserId(userId);
        return accountRepository.save(account);
    }

    // Update an account based on userId
    public int updateAccount(int id, String userId, Account.AccountType type, String accountNumber,
            String routingNumber, String institution, BigDecimal investmentRate,
            BigDecimal startingBalance) {
        return accountRepository.updateAccount(id, userId, type, accountNumber, routingNumber, institution,
                investmentRate, startingBalance);
    }

    // Delete Account based on userId
    public void deleteAccount(int id, String userId) {
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(a -> {
            if (a.getUserId().equals(userId)) {
                accountRepository.deleteById(id);
            }
        });
    }

}
