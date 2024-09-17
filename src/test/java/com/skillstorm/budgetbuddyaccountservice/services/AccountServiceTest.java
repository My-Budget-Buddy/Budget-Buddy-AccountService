package com.skillstorm.budgetbuddyaccountservice.services;

//Dependency packages
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Transaction;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.exceptions.AccountNotFoundException;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
//Mockito
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.junit.jupiter.api.AfterEach;
//Junit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

//Java utils
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.math.*;
import java.net.URI;
import java.time.LocalDate;

public class AccountServiceTest {

    @Mock
    private RestClient restClient;
    @Mock
    ServiceInstance serviceInstance;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;
    
    @Mock
    private LoadBalancerClient loadBalancerClient;

    @InjectMocks
    private AccountService accountService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void teardown() throws Exception{
        closeable.close();
    }
    //

    @Test 
    public void testGetTransactionByUserId(){
        String userId = "123";
        String transactionServiceUrl = "http://transaction_service";

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(123,123456, "abc Company", 100 , "Food" , "Lunch", LocalDate.now()));

        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        //need to mock loadbalancer since this method call is owned by the AccountService
        when(serviceInstance.getInstanceId()).thenReturn("transaction-service");
        when(serviceInstance.getUri()).thenReturn(URI.create(transactionServiceUrl));
        when(serviceInstance.getServiceId()).thenReturn("transaction-service");


        when(loadBalancerClient.choose("transaction-service")).thenReturn(serviceInstance);

        when(accountService.getTransactionsByUserId(userId)).thenReturn(mockTransactions);
        


        List<Transaction> transactions = accountService.getTransactionsByUserId(userId);

        //Assert
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
    }

    @Test
    public void testGetAccountsByUserId() {
        // Arrange
        String userId = "123";
        List<Account> mockAccounts = new ArrayList<>();
        Account mockAccount = new Account(userId, AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500));
        mockAccounts.add(mockAccount);

        
        when(accountRepository.findByUserId(userId)).thenReturn(mockAccounts);

        // Mock the AccountMapper to return a non-null AccountDto
        AccountDto mockAccountDto = new AccountDto(userId, AccountType.CHECKING, "123456", "00001", "Bank A",
        new BigDecimal(5), new BigDecimal(1500), new BigDecimal(1500));
        when(accountMapper.toDto(mockAccount)).thenReturn(mockAccountDto);

        //need to create a transaction and then mock handing over list to transaction service
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(123,123456, "abc Company", 100 , "Food" , "Lunch", LocalDate.now()));
        
        AccountService accountServiceSpy = spy(accountService);
        doReturn(mockTransactions).when(accountServiceSpy).getTransactionsByUserId(userId);

        // All our Mocks are setup, call our method under test
        List<AccountDto> accountDtos = accountServiceSpy.getAccountsByUserId(userId);
        System.out.println("we are past the mock of the method");
        // Assert
        assertNotNull(accountDtos);
        assertEquals(1, accountDtos.size());
        verify(accountRepository, times(1)).findByUserId(userId);
    }

    // @Disabled
    // @Test
    // public void testSaveAccount_Success() {
    //     // Arrange
    //     AccountDto accountDto = new AccountDto("user123", AccountType.SAVINGS, "123456", "654321", "Bank B", BigDecimal.valueOf(2.5), BigDecimal.valueOf(1000), BigDecimal.valueOf(1500));
    //     mockAccounts.add(new Account(userId,AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500)));

    //     when(accountMapper.toEntity(accountDto)).thenReturn(account);
    //     when(accountRepository.save(account)).thenReturn(account);

    //     // Act
    //     AccountDto result = accountService.saveAccount(accountDto);

    //     // Assert
    //     assertNotNull(result);
    //     verify(accountRepository, times(1)).save(account);
    //     verify(accountMapper, times(1)).toEntity(accountDto);
    // }
    // @Disabled
    // @Test
    // public void testGetAccountsByUserId_AccountNotFound() {
    //     // Arrange
    //     String userId = "user123";
    //     when(accountRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

    //     // Act
    //     List<AccountDto> accountDtos = accountService.getAccountsByUserId(userId);

    //     // Assert
    //     assertTrue(accountDtos.isEmpty());
    //     verify(accountRepository, times(1)).findByUserId(userId);
    // }
    // @Disabled
    // @Test
    // public void testVerifyAccountOwnership_Success() {
    //     // Arrange
    //     String userId = "user123";
    //     int accountId = 1;
    //     Account account = new Account(1, userId, "123456", "Bank B", BigDecimal.valueOf(2.5), BigDecimal.valueOf(1000));
    //     when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

    //     // Act & Assert
    //     assertDoesNotThrow(() -> accountService.verifyAccountOwnership(userId, accountId));
    //     verify(accountRepository, times(1)).findById(accountId);
    // }
    // @Disabled
    // @Test
    // public void testVerifyAccountOwnership_AccountNotFound() {
    //     // Arrange
    //     String userId = "user123";
    //     int accountId = 1;
    //     when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

    //     // Act & Assert
    //     assertThrows(AccountNotFoundException.class, () -> accountService.verifyAccountOwnership(userId, accountId));
    //     verify(accountRepository, times(1)).findById(accountId);
    // }
}