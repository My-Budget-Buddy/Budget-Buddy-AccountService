package com.skillstorm.budgetbuddyaccountservice.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
