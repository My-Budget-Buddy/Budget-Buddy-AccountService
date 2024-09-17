package com.skillstorm.budgetbuddyaccountservice.services;

//Dependency packages
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Transaction;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.exceptions.*;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;


//Field injection
import java.lang.reflect.Field;

//Mockito
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.*;
//cloud libraries
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.*;

//Junit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

//Java utils
import java.util.*;
import java.math.*;
import java.net.URI;
import java.time.LocalDate;

public class AccountServiceTest {

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
    public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() throws Exception{
        closeable.close();
    }
    
    //complete
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test 
    public void testGetTransactionByUserId() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        String userId = "123";
        String transactionServiceUrl = "http://transaction_service";

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(123,123456, "abc Company", 100 , "Food" , "Lunch", LocalDate.now()));

        //init mocks for request/response
        RestClient restClient = mock(RestClient.class);
        ServiceInstance serviceInstance = mock(ServiceInstance.class);

        //Since RestClient is alrady built in constructor we need to reinject with uninstantiated
        Field restClientField = AccountService.class.getDeclaredField("restClient");
        restClientField.setAccessible(true);
        restClientField.set(accountService, restClient);

        when(serviceInstance.getUri()).thenReturn(URI.create(transactionServiceUrl));
        when(serviceInstance.getServiceId()).thenReturn("transaction-service");

        when(loadBalancerClient.choose("transaction-service")).thenReturn(serviceInstance);

         //setup rest client chaining
        RequestHeadersUriSpec requestHeadersUriMock = mock(RestClient.RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeaderMock = mock(RestClient.RequestHeadersSpec.class);
        ResponseSpec responseMock = mock(RestClient.ResponseSpec.class);
        
        when(restClient.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(anyString())).thenReturn(requestHeaderMock);
        when(requestHeaderMock.retrieve()).thenReturn(responseMock);
        when(responseMock.body(any(ParameterizedTypeReference.class))).thenReturn(mockTransactions);

        //Run test
        List<Transaction> transactions = accountService.getTransactionsByUserId(userId);
        System.out.println(transactions.get(0).toString());

        Transaction testTransaction = new Transaction(123,123456, "abc Company", 100 , "Food" , "Lunch", LocalDate.now());
        //Assert
        assertFalse(transactions.isEmpty());
        assertEquals(testTransaction, transactions.get(0));
    }

    //complete
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
        // Assert
        assertNotNull(accountDtos);
        assertEquals(1, accountDtos.size());
        verify(accountRepository, times(1)).findByUserId(userId);
    }
    //complete
    @Test
    public void testCreateAccount_all_errors() {
        // branch where we encounter errors for all
        //create account
        Account account = new Account("123", null, null, "432434234", null, new BigDecimal(0), new BigDecimal(0));

        //no need to mock account since it's all null
        String expectedErrorMessage = "NotEnoughInformationException: No account number, institution or type";

        NotEnoughInformationException exception = assertThrows(NotEnoughInformationException.class, () -> 
        accountService.createAccount(account, "123"));
        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());

    }
    //complete
    @Test
    public void testCreateAccount_one_errors() {
        // branch where we encounter errors no accountnumber
        //create account with null accountnumber
        Account account = new Account("123", AccountType.CHECKING, null, "432434234", "The Bank", new BigDecimal(0), new BigDecimal(0));

        String expectedErrorMessage = "NotEnoughInformationException: No account number";

        NotEnoughInformationException exception = assertThrows(NotEnoughInformationException.class, () -> 
        accountService.createAccount(account, "123"));
        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());

    }
    //in-progress
    @Test
    public void testCreateAccount(){
        String userId = "123";

        // Create an account with null starting balance to test the null condition branch
        Account account = new Account("123", AccountType.CHECKING, "4239434493", 
        "0000021", "The Bank", new BigDecimal(0), null);

        // Account after setting starting balance to 0 and assuming the ID will be generated by the repository
        Account completeAccount = new Account("123", AccountType.CHECKING, "4239434493", 
                    "0000021", "The Bank", new BigDecimal(0), new BigDecimal(0));
        completeAccount.setId(0); // Set the expected ID after save
        completeAccount.setUserId(userId);
        // Create the expected AccountDto result
        AccountDto testResult = new AccountDto("123", AccountType.CHECKING, "4239434493", 
                    "0000021", "The Bank", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
        // Mock the repository save method to return the 'completeAccount'
        when(accountRepository.save(any(Account.class))).thenReturn(completeAccount);
    
        // Spy the AccountService and mock the method getAccountByAccountIdAndUserId
        AccountService accountServiceSpy = spy(accountService);
    
        // Correctly mock getAccountByAccountIdAndUserId to return Optional.of(testResult)
        doReturn(Optional.of(testResult)).when(accountServiceSpy).getAccountByAccountIdAndUserId(userId, completeAccount.getId());
        
         //init test
        AccountDto result = accountService.createAccount(account, userId);
        
        // Asserts
        assertNotNull(result);
        //assertEquals(testResult, result);

        //verify repository
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    //this is for reference ignore, delete after testcreate is done
    @Disabled
    @Test 
    public void testCreateAccount_no_errors() {
        Account account = new Account("123", AccountType.CHECKING, "4239434493", "432434234", "The Bank", new BigDecimal(0), new BigDecimal(0));
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findByUserId(any(String.class))).thenReturn(accounts);
        when(accountRepository.findById(any(int.class))).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountMapper mapper = new AccountMapper();
        when(accountMapper.toDto(any(Account.class))).thenReturn(mapper.toDto(account));
        RestClient restClient = mock(RestClient.class);
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        when(loadBalancerClient.choose(any(String.class))).thenReturn(serviceInstance);
  
        List<AccountDto> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(mapper.toDto(account));

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