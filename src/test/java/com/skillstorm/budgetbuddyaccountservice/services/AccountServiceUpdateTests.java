package com.skillstorm.budgetbuddyaccountservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUpdateTests {

    @InjectMocks
    private static AccountService accountService;

    @Mock
    private static AccountRepository accountRepository;

    @Mock
    private static AccountMapper accountMapper;

    @Mock
    private static LoadBalancerClient loadBalancerClient;

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

    @Test
    public void updateAccountTest() {
        ServiceInstance serviceInstance = new TestServiceInstance();
        when(loadBalancerClient.choose(any(String.class))).thenReturn(serviceInstance);

        Account account = new Account(
                "123",
                AccountType.CHECKING,
                "4239434493",
                "432434234",
                "The Bank",
                new BigDecimal(0),
                new BigDecimal(0));

        Optional<Account> accountOptional = Optional.of(account);
        when(accountRepository.findById(any(int.class))).thenReturn(accountOptional);

        AccountMapper mapper = new AccountMapper();
        when(accountMapper.toDto(any(Account.class))).thenReturn(mapper.toDto(account));

        accountService.updateAccount(1, "123", AccountType.SAVINGS, "32432434", "32434234", "The Bank 2",
                new BigDecimal(0), new BigDecimal(0));

        Optional<AccountDto> expectedAccounts = Optional.of(accountMapper.toDto(account));

        assertEquals(expectedAccounts, accountService.getAccountByAccountIdAndUserId("123", 1));
    }

}
