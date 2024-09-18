package com.skillstorm.budgetbuddyaccountservice.controllers;


//local packages
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

//spring framework
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
//Mockito
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


//Junit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.checkerframework.checker.units.qual.h;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.*;
//util
import java.util.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        // Clean up mocks
        closeable.close();
    }
    //complete
    //@Disabled
    @Test
    public void testGetAccountsByUserId() {
        String userId = "1";
        //setup mock data
        List<AccountDto> accountsDTO = new ArrayList<>();
        AccountDto mockDTO = new AccountDto(userId, Account.AccountType.CHECKING, "123456789", 
                        "987654321", "Bank of America", new BigDecimal(0.01), new BigDecimal(1000), new BigDecimal(1000));
        accountsDTO.add(mockDTO);
        
        HttpHeaders httpHeaders = mock(HttpHeaders.class);
        
        when(httpHeaders.getFirst("User-ID")).thenReturn(userId);
        when(accountService.getAccountsByUserId(userId)).thenReturn(accountsDTO);

        //test
        ResponseEntity<List<AccountDto>> response = accountController.getAccountsByUserId(httpHeaders);
        
        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountsDTO, response.getBody());
    }
    //complete
    //@Disabled
    @Test
    public void testGetAccountByAccountIdAndUserId() {
        int Id = 1;
        String userId = "123";

        HttpHeaders httpHeaders = mock(HttpHeaders.class);
        
        AccountDto mockAccount = new AccountDto(Id, userId, Account.AccountType.CHECKING, "123456789", 
        "987654321", "Bank of America", new BigDecimal(0.01), new BigDecimal(1000), new BigDecimal(1000));

        when(httpHeaders.getFirst("User-ID")).thenReturn(userId);
        when(accountService.getAccountByAccountIdAndUserId(userId, Id)).thenReturn(Optional.of(mockAccount));

        ResponseEntity<AccountDto> response = accountController.getAccountByAccountIdAndUserId(Id, httpHeaders);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccount, response.getBody());
    }
    //complete
    //@Disabled
    @Test
    public void testGetAccountByAccountIdAndUserId_notpresent() {
        //check to see not found status sent
        int Id = 1;
        String userId = "123";

        HttpHeaders httpHeaders = mock(HttpHeaders.class);
        
        when(httpHeaders.getFirst("User-ID")).thenReturn(userId);
        when(accountService.getAccountByAccountIdAndUserId(userId, Id)).thenReturn(Optional.empty());
        
        ResponseEntity<AccountDto> response = accountController.getAccountByAccountIdAndUserId(Id, httpHeaders);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        //assertEquals(mockAccount, response.getBody());
    }
    //in-progress
    //@Disabled
    @Test
    public void testCreateAccount() {
        int Id = 1;
        String userId = "user123";

        HttpHeaders httpHeaders = mock(HttpHeaders.class);
        AccountDto mockDTO = new AccountDto(Id, userId, Account.AccountType.CHECKING, "123456789", 
        "987654321", "Bank of America", new BigDecimal(0.01), new BigDecimal(1000), new BigDecimal(1000));
        Account mockAccount = new Account(Id, userId, Account.AccountType.CHECKING, "123456789", 
        "987654321", "Bank of America", new BigDecimal(0.01), new BigDecimal(1000));
        
        when(httpHeaders.getFirst("User-ID")).thenReturn(userId);

        when(any(HttpHeaders.class).getFirst("User-ID")).thenReturn(userId);
        when(accountService.createAccount(mockAccount, userId)).thenReturn(mockDTO);

        ResponseEntity<AccountDto> response = accountController.createAccount(mockAccount, any(HttpHeaders.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockDTO, response.getBody());
    }
    @Disabled
    @Test
    public void testUpdateAccount() {
        String userId = "user123";
        int accountId = 1;
        Account accountDetails = new Account();
        when(any(HttpHeaders.class).getFirst("User-ID")).thenReturn(userId);
        when(accountService.updateAccount(accountId, userId, accountDetails.getType(), accountDetails.getAccountNumber(),
                accountDetails.getRoutingNumber(), accountDetails.getInstitution(), accountDetails.getInvestmentRate(),
                accountDetails.getStartingBalance())).thenReturn(1);

        ResponseEntity<Integer> response = accountController.updateAccount(accountId, accountDetails, any(HttpHeaders.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Disabled
    @Test
    public void testDeleteAccount() {
        String userId = "user123";
        int accountId = 1;
        when(any(HttpHeaders.class).getFirst("User-ID")).thenReturn(userId);

        ResponseEntity<Void> response = accountController.deleteAccount(accountId, any(HttpHeaders.class));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Disabled
    @Test
    public void testDeleteAllAccounts() {
        String userId = "user123";
        when(any(HttpHeaders.class).getFirst("User-ID")).thenReturn(userId);

        ResponseEntity<Void> response = accountController.deleteAllAccounts(any(HttpHeaders.class));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
