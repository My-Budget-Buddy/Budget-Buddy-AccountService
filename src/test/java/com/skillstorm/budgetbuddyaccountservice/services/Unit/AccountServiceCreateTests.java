package com.skillstorm.budgetbuddyaccountservice.services.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceCreateTests {

    //code from original team, not sure what purpose is yet
    private class TestServiceInstance implements ServiceInstance {

        @Override
        public String getServiceId() {
            throw new UnsupportedOperationException("Unimplemented method 'getServiceId'");
        }

        @Override
        public String getHost() {
            throw new UnsupportedOperationException("Unimplemented method 'getHost'");
        }

        @Override
        public int getPort() {
            throw new UnsupportedOperationException("Unimplemented method 'getPort'");
        }

        @Override
        public boolean isSecure() {
            throw new UnsupportedOperationException("Unimplemented method 'isSecure'");
        }

        @Override
        public URI getUri() {
            try {
                URI uri = new URI("http://localhost:8083");
                return uri;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Map<String, String> getMetadata() {
            throw new UnsupportedOperationException("Unimplemented method 'getMetadata'");
        }

    }
    //account service needs loadbalancer client, account repository, and account mapper to instantiate
    @Mock
    private RestClient RestClient;

    @Mock
    private static AccountRepository accountRepository;

    @Mock
    private static AccountMapper accountMapper;

    @Mock
    private static LoadBalancerClient loadBalancerClient;

    @InjectMocks
    private static AccountService accountService;


    //proper setup/teardown of inject mock
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown(){
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
    }

    @Test
    public void testCreateAccount() {
        // Setup Mocks
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Mocked Response");
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findByUserId(any(String.class))).thenReturn(accounts);
        when(accountRepository.findById(any(int.class))).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
    
        AccountMapper mapper = new AccountMapper();
        AccountDto expectedAccountDto = mapper.toDto(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);
    
        ServiceInstance serviceInstance = new TestServiceInstance();
        when(loadBalancerClient.choose(any(String.class))).thenReturn(serviceInstance);
    
        // Call createAccount Method
        AccountDto newAccount = accountService.createAccount(account, "123");
    
        // Assertions
        assertNotNull(newAccount, "The new account should not be null");
        assertEquals(expectedAccountDto.getId(), newAccount.getId(), "The account ID should match");
        assertEquals(expectedAccountDto.getName(), newAccount.getName(), "The account name should match");
        // Add more assertions as needed to verify other properties of AccountDto
    
        // Additional checks for getAccountsByUserId method
        List<AccountDto> actualAccounts = accountService.getAccountsByUserId("123");
        assertNotNull(actualAccounts, "The account list should not be null");
        assertFalse(actualAccounts.isEmpty(), "The account list should not be empty");
        assertEquals(1, actualAccounts.size(), "The account list size should be 1");
        assertEquals(account.getId(), actualAccounts.get(0).getId(), "The account ID should match");
    }

}
