package com.skillstorm.budgetbuddyaccountservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @InjectMocks
    private static AccountService accountService;

    @Mock
    private static AccountRepository accountRepository;

    @Mock
    private static AccountMapper accountMapper;

    @Test
    public void createAccountTest() {
        Account account = new Account(
            "123",
            AccountType.CHECKING,
            "4239434493",
            "432434234",
            "The Bank",
            new BigDecimal(0),
            new BigDecimal(0)
        );

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findByUserId(any(String.class))).thenReturn(accounts);

        AccountMapper mapper = new AccountMapper();
        when(accountMapper.toDto(any(Account.class))).thenReturn(mapper.toDto(account));

        accountService.createAccount(account, "123");

        List<AccountDto> actualAccounts = accountService.getAccountsByUserId("123");

        List<AccountDto> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(accountMapper.toDto(account));

        assertEquals(expectedAccounts, actualAccounts);
    }

}