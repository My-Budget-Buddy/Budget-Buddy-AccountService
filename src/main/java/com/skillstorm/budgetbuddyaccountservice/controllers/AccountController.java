package com.skillstorm.budgetbuddyaccountservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get Accounts by userId
    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> getAccountsByUserId(@PathVariable String userId) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Get Account by accountId and userId
    @GetMapping("/{userId}/{id}")
    public ResponseEntity<AccountDto> getAccountByAccountIdAndUserId(@PathVariable String userId, @PathVariable int id) {
        Optional<AccountDto> account = accountService.getAccountByAccountIdAndUserId(userId, id);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create Account
    @PostMapping("/{userId}")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account account, @PathVariable String userId) {
        AccountDto createdAccount = accountService.createAccount(account, userId);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Update Account
    @PutMapping("/{userId}/{id}")
    public ResponseEntity<Integer> updateAccount(@PathVariable String userId, @PathVariable int id,
            @RequestBody Account accountDetails) {
        int updated = accountService.updateAccount(id, userId, accountDetails.getType(),
                accountDetails.getAccountNumber(), accountDetails.getRoutingNumber(),
                accountDetails.getInstitution(), accountDetails.getInvestmentRate(),
                accountDetails.getStartingBalance());
        if (updated > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Account
    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String userId, @PathVariable int id) {
        accountService.deleteAccount(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete all accounts
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAllAccounts(@PathVariable String userId) {
        accountService.deleteAllAccounts(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
