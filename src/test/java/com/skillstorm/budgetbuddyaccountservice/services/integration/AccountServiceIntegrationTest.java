package com.skillstorm.budgetbuddyaccountservice.services.integration;
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.exceptions.AccountNotFoundException;
import com.skillstorm.budgetbuddyaccountservice.exceptions.IdMismatchException;
import com.skillstorm.budgetbuddyaccountservice.exceptions.NotEnoughInformationException;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.models.Transaction;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    // Mocking repository b/c currently testing and development are utilizing the same repository
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private LoadBalancerClient loadBalancerClient;

    /*
     * When test runs successfully we get a serialized restClient response which is a empty list of transactions.
     */
    @Test
    public void getTransactionByUserIdITest() {
        // Arrange
        String userId = "123";
        List<Transaction> expected = new ArrayList<>();
        when(loadBalancerClient.choose(any(String.class))).thenReturn(getServiceInstance());        
        // Act
        List<Transaction> actual = accountService.getTransactionsByUserId(userId);
        // Assert
        assertEquals(expected, actual);
        
    }

    /*
     * Test will throw a IllegalStateException if ServiceInstance is null from getTransactionByUserId
     */
    @Test
    public void getAccountByUserIdThrowsIllegalStateExceptionITest() {
        // Arrange
        String userId = "123";
        List<Account> listOfAccounts = new ArrayList<>();
        Account account = new Account(userId, AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500));
        listOfAccounts.add(account);

        when(accountRepository.findByUserId(userId)).thenReturn(listOfAccounts);
        when(loadBalancerClient.choose("transaction_service")).thenReturn(null);
        // Act and Assert
        Exception actual = assertThrows(IllegalStateException.class, () -> accountService.getAccountsByUserId(userId));
        assert(actual.getMessage().contains("No instances available for transaction_service"));

    }

    /*
     * Test will return empty Optional if account id does not exist in repo
     */
    @Test
    public void getAccountByAccountIdAndUserIdEmptyOptionalITest() {
        // Arrange
        Optional<Account> account = Optional.empty();
        when(accountRepository.findById(anyInt())).thenReturn(account);
        // Act
        Optional<AccountDto> actual = accountService.getAccountByAccountIdAndUserId( "1", 1);
        // Assert
        assertEquals(Optional.empty(), actual);

    }

    /* 
     * Test will throw an exception because the account object does not have at least 2/3 of account number, routing number, or bank name set
     */
    @Test
    public void createAccountThrowsNotEnoughInformationExceptionITest() {
        // Arrange
        Account account = new Account();
        // Act and Assert
        assertThrows(NotEnoughInformationException.class, () -> accountService.createAccount(account, "1"));

    }

    /*
     * Test successfull if header has a User-ID value
     */
    @Test
    public void validateRequestHeadersITest() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        // Act and Assert
        assertDoesNotThrow( () -> accountService.validateRequestWithHeaders(headers));
    }

    /*
     * Test throws IdMismatchException if header does not have a User-ID value
     */
    @Test
    public void validateRequestHeadersThrowsIdMismatchExceptionITest() {
        // Arrange  no header matching User-ID provided
        HttpHeaders headers = new HttpHeaders();
        // Act and Assert
        assertThrows(IdMismatchException.class, () ->  accountService.validateRequestWithHeaders(headers));
    }

    /*
     * Test for a successful update of an account
     */
    @Test   
    public void updateAccountITest() {
        // Arrange
        Account account = new Account();
        account.setUserId("1");

        Optional<Account> accountOptional = Optional.of(account);
        when(accountRepository.findById(anyInt())).thenReturn(accountOptional);
        when(accountRepository.updateAccount(anyInt(),anyString(),any(Account.AccountType.class),
                        anyString(),anyString(),anyString(),any(BigDecimal.class),any(BigDecimal.class)))
                        .thenReturn(1);
        // Act
        int actual = accountService.updateAccount(1, "1", AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500));
        // Assert
        assertEquals(1, actual);
        
    }

    /*
     * Test will throw a NullPointerException if the list of accounts is null
     */
    @Test
    public void deleteAllAccountsThrowsNullPointerExceptionITest(){
        // Arrange
        String userId = "1";
        List<Account> listOfAccounts = null;    
        when(accountRepository.findByUserId(userId)).thenReturn(listOfAccounts);
        // Act and Assert
        assertThrows(NullPointerException.class, () -> accountService.deleteAllAccounts(userId));
    }

    /*
     * Test will successfully delete all accounts if the list of accounts is suppllied
     */
    @Test
    public void deleteAllAccountsITest(){
        // Arrange
        String userId = "1";
        List<Account> listOfAccounts = new ArrayList<>();    
        when(accountRepository.findByUserId(userId)).thenReturn(listOfAccounts);
        doNothing().when(accountRepository).deleteById(anyInt());
        // Act and Assert
        accountService.deleteAllAccounts(userId);
        // Does not get called because the list of accounts is empty
        verify(accountRepository, times(0)).deleteById(anyInt());
    }

    /*
     * Test will successfully delete all accounts if the list of accounts is suppllied
     */
    @Test
    public void deleteAllAccountsWithArrayITest(){
        // Arrange
        String userId = "1";
        List<Account> listOfAccounts = new ArrayList<>();    
        Account account = new Account(userId, AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500));
        Account secondAccount = new Account("2", AccountType.CHECKING, "123956", "00002", "Bank B", new BigDecimal(5), new BigDecimal(1500));
        listOfAccounts.add(account);
        listOfAccounts.add(secondAccount);
        when(accountRepository.findByUserId(userId)).thenReturn(listOfAccounts);
        doNothing().when(accountRepository).deleteById(anyInt());
        // Act and Assert
        accountService.deleteAllAccounts(userId);
        // Gets called twice because there are two accounts in the list
        verify(accountRepository, times(2)).deleteById(anyInt());
    }


    /* 
     * Test will throw an AccountNotFoundException if the account object is null
     */
    @Test
    public void deleteAccountThrowsAccountNotFoundExceptionITest(){
        // Arrange
        int id = 1;
        String userId = "1";

        Optional<Account> account = Optional.ofNullable(null);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        // Act and Assert
        Exception actual = assertThrows(AccountNotFoundException.class, () ->accountService.deleteAccount(id, userId));
        assert(actual.getMessage().contains("Account with ID " + userId + " not found."));
    }

    /*
     * Test will throw a NullPointerException if the account object exist but has no data
     */
    @Test
    public void deleteAccountThrowsNullPointerExceptionITest(){
        // Arrange
        int id = 1;
        String userId = "1";
        Account existingAccount = new Account();
        Optional<Account> account = Optional.ofNullable(existingAccount);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        // Act and Assert
        assertThrows(NullPointerException.class, () ->accountService.deleteAccount(id, userId));

    }

    /*
     * Test will throw a IdMismatchException if the userId does not match the account object userId
     */
    @Test
    public void deleteAccountIdMismatchExceptionITest(){
        // Arrange
        int id = 1;
        String userId = "2";
        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setUserId("3"); // setting a different userId vs variable
        existingAccount.setType(AccountType.CHECKING);
        existingAccount.setAccountNumber("123456");
        existingAccount.setRoutingNumber("00001");
        existingAccount.setInstitution("Bank A");
        existingAccount.setInvestmentRate(new BigDecimal(5));
        existingAccount.setStartingBalance(new BigDecimal(1500));

        Optional<Account> account = Optional.ofNullable(existingAccount);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        doNothing().when(accountRepository).deleteById(anyInt());
        // Act and Assert
        assertThrows(IdMismatchException.class, () -> accountService.deleteAccount(id, userId));

    }

    /*
     * Test successfully deletes an account if the account object exist and has data
     */
    @Test
    public void deleteAccountITest(){
        // Arrange
        int id = 1;
        String userId = "2";
        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setUserId(userId);
        existingAccount.setType(AccountType.CHECKING);
        existingAccount.setAccountNumber("123456");
        existingAccount.setRoutingNumber("00001");
        existingAccount.setInstitution("Bank A");
        existingAccount.setInvestmentRate(new BigDecimal(5));
        existingAccount.setStartingBalance(new BigDecimal(1500));

        Optional<Account> account = Optional.of(existingAccount);
        when(accountRepository.findById(anyInt())).thenReturn(account);
        doNothing().when(accountRepository).deleteById(anyInt());
        // Act and Assert
        accountService.deleteAccount(id, userId);
        verify(accountRepository, times(1)).deleteById(anyInt());

    }

    // Static helper method to create a ServiceInstance object
    public static ServiceInstance getServiceInstance(){
        return new ServiceInstance() {
            @Override
            public String getServiceId() {
                return "testService";
            }

            @Override
            public String getHost() {
                return "localhost";
            }

            @Override
            public int getPort() {
                return 8080;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public URI getUri() {
                return URI.create("http://localhost:8080");
            }

            @Override
            public Map<String, String> getMetadata() {
                return Collections.emptyMap();
            }
        };
    }

}
