package com.skillstorm.budgetbuddyaccountservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccountsByUserId(@RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        // Get the user ID from headers, this shouldn't be null anymore since we
        // validate the request above
        // another option is to have validateRequestWithHeaders return the userID
        // string.
        String userId = httpHeaders.getFirst("User-ID");

        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Get individual account by ID
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountByAccountIdAndUserId(@PathVariable int accountId,
            @RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        String userId = httpHeaders.getFirst("User-ID");

        Optional<AccountDto> account = accountService.getAccountByAccountIdAndUserId(userId, accountId);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create Account
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account account,
            @RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        String userId = httpHeaders.getFirst("User-ID");

        AccountDto createdAccount = accountService.createAccount(account, userId);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Update Account
    @PutMapping("/{accountId}")
    public ResponseEntity<Integer> updateAccount(@PathVariable int accountId,
            @RequestBody Account accountDetails, @RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        String userId = httpHeaders.getFirst("User-ID");

        int updated = accountService.updateAccount(accountId, userId, accountDetails.getType(),
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
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable int accountId,
            @RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        String userId = httpHeaders.getFirst("User-ID");

        accountService.deleteAccount(accountId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete all accounts
    @DeleteMapping
    public ResponseEntity<Void> deleteAllAccounts(@RequestHeader HttpHeaders httpHeaders) {

        accountService.validateRequestWithHeaders(httpHeaders);

        String userId = httpHeaders.getFirst("User-ID");

        accountService.deleteAllAccounts(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
