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
    //completed
    //@Disabled
    @Test
    public void testGetAccountByAccountIdAndUserId_Success() {
        //apparently ID needs to match the AccountID
        String userId = "user123";
        int Id = 123456;

        Account mockAccount = new Account(Id, userId,AccountType.CHECKING, "123456", "00001",
                                 "Bank A", new BigDecimal(5), new BigDecimal(1500));


        when(accountRepository.findById(Id)).thenReturn(Optional.of(mockAccount));

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(123, Id, "abc Company", 100 , "Food" , "Lunch", LocalDate.now()));
        
        AccountService accountServiceSpy = spy(accountService);
        doReturn(mockTransactions).when(accountServiceSpy).getTransactionsByUserId(userId);
        
        AccountDto mockDto = new AccountDto(Id, userId, AccountType.CHECKING, "123456", "00001",
        "Bank A", new BigDecimal(5), new BigDecimal(1500),new BigDecimal(0));
        when(accountMapper.toDto(any(Account.class))).thenReturn(mockDto);

        Optional<AccountDto> result = accountServiceSpy.getAccountByAccountIdAndUserId(userId, Id);

        AccountDto testResult = result.get();
        System.out.println(testResult.getAccountNumber());
        assertTrue(result.isPresent());
        assertEquals("**3456", testResult.getAccountNumber());
    }
    //complete
    @SuppressWarnings({ "unchecked", "rawtypes" })
    //@Disabled
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
    //@Disabled
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
    //@Disabled
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
    //@Disabled
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
    //complete
    //@Disabled
    @Test
    public void testCreateAccount_no_error() {
        String userId = "123";
        int Id = 123456;
    
        // Create an account with null starting balance to test the null condition branch
        Account mockaccount = new Account(Id, "123", AccountType.CHECKING, "4239434493", 
                                      "0000021", "The Bank", new BigDecimal(0), null);
    
        // Account after setting starting balance to 0 and assuming the ID will be generated by the repository
        Account completeAccount = new Account(Id, "123", AccountType.CHECKING, "4239434493", 
                                              "0000021", "The Bank", new BigDecimal(0), new BigDecimal(0)); 
                                              // Set the expected ID after save
    
        // Create the expected AccountDto result
        AccountDto testResult = new AccountDto(Id, "123", AccountType.CHECKING, "4239434493", 
                                               "0000021", "The Bank", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
    
        // Mock the repository save method to return the 'completeAccount' with ID set
        when(accountRepository.save(any(Account.class))).thenReturn(completeAccount);
    
        // Mock findById in accountRepository to return the complete account
        when(accountRepository.findById(completeAccount.getId())).thenReturn(Optional.of(completeAccount));
    
        // Spy the AccountService so real logic of createAccount can be called
        AccountService accountServiceSpy = spy(accountService);
    
        // Mock getAccountByAccountIdAndUserId to return the AccountDto
        doReturn(Optional.of(testResult)).when(accountServiceSpy).getAccountByAccountIdAndUserId(userId, completeAccount.getId());
    
        // Mock getTransactionsByUserId (if needed)
        //when(accountServiceSpy.getTransactionsByUserId(userId)).thenReturn(Collections.emptyList());
    
        // Execute the test (real createAccount logic will run)
        AccountDto result = accountServiceSpy.createAccount(mockaccount, userId);
    
        // Assert the results
        assertNotNull(result);  // Ensures the result is not null
        assertEquals(testResult, result);  // Asserts the result is what we expect
    
        // Verify that save was called once with any Account object
        verify(accountRepository, times(1)).save(any(Account.class));
    
        // Verify that getAccountByAccountIdAndUserId was called with the correct parameters
        verify(accountServiceSpy, times(1)).getAccountByAccountIdAndUserId(userId, completeAccount.getId());
    }
}
