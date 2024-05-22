package com.skillstorm.budgetbuddyaccountservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private AutoCloseable closeable;

    @Autowired
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    @Test
    public void testGetAccountsByUserId() throws Exception {
        String userId = "user123";
        List<AccountDto> accounts = Arrays.asList(new AccountDto(), new AccountDto());
        when(accountService.getAccountsByUserId(userId)).thenReturn(accounts);

        mockMvc.perform(get("/accounts/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(accounts.size()));

        verify(accountService, times(1)).getAccountsByUserId(userId);
    }

    @Test
    public void testGetAccountByAccountIdAndUserId() throws Exception {
        String userId = "user123";
        int accountId = 1;

        AccountDto accountDto = new AccountDto();
        accountDto.setId(accountId);
        accountDto.setUserId(userId);

        Optional<AccountDto> accountOptional = Optional.of(accountDto);

        when(accountService.getAccountByAccountIdAndUserId(userId, accountId)).thenReturn(accountOptional);

        mockMvc.perform(get("/accounts/{userId}/{id}", userId, accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDto.getId()))
                .andExpect(jsonPath("$.userId").value(userId));

        verify(accountService, times(1)).getAccountByAccountIdAndUserId(userId, accountId);
    }

    
    @Test
    public void testGetAccountByAccountIdAndUserIdNotFound() throws Exception {
        String userId = "user123";
        int accountId = 1;

        when(accountService.getAccountByAccountIdAndUserId(userId, accountId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/accounts/{userId}/{id}", userId, accountId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAccount() throws Exception {
        String userId = "user123";

       
        Account account = new Account();
        account.setUserId(userId);
        account.setType(Account.AccountType.CHECKING);
        account.setAccountNumber("123456789");
        account.setRoutingNumber("987654321");
        account.setInstitution("Bank");
        account.setInvestmentRate(BigDecimal.valueOf(0.05));
        account.setStartingBalance(BigDecimal.valueOf(1000));

        AccountMapper mapper = new AccountMapper();
        AccountDto accountDto = mapper.toDto(account);
        accountDto.setAccountNumber("*****6789");
        accountDto.setRoutingNumber("*****4321");
        accountDto.setCurrentBalance(accountDto.getStartingBalance());
        when(accountService.createAccount(any(Account.class), eq(userId))).thenReturn(accountDto);

        mockMvc.perform(post("/accounts/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user123\"," +
                        "\"type\":\"CHECKING\"," +
                        "\"accountNumber\":\"123456789\"," +
                        "\"routingNumber\":\"987654321\"," +
                        "\"institution\":\"Bank\"," +
                        "\"investmentRate\":0.05," +
                        "\"startingBalance\":1000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId));

        verify(accountService, times(1)).createAccount(any(Account.class), eq(userId));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        String userId = "user123";
        int accountId = 1;
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(userId);
        account.setType(Account.AccountType.CHECKING);
        account.setAccountNumber("123456789");
        account.setRoutingNumber("987654321");
        account.setInstitution("Bank");
        account.setInvestmentRate(BigDecimal.valueOf(0.05));
        account.setStartingBalance(BigDecimal.valueOf(1000));

        when(accountService.updateAccount(eq(accountId), eq(userId), any(Account.AccountType.class),
                anyString(), anyString(), anyString(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(1);

        mockMvc.perform(put("/accounts/{userId}/{id}", userId, accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"userId\":\"user123\",\"type\":\"CHECKING\",\"accountNumber\":\"123456789\"," +
                        "\"routingNumber\":\"987654321\",\"institution\":\"Bank\",\"investmentRate\":0.05," +
                        "\"startingBalance\":1000}"))
                .andExpect(status().isOk());

        verify(accountService, times(1)).updateAccount(eq(accountId), eq(userId), any(Account.AccountType.class),
                anyString(), anyString(), anyString(), any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    public void testUpdateAccountNotFound() throws Exception {
        String userId = "user123";
        int accountId = 1;

        Account accountDetails = new Account();
        accountDetails.setType(Account.AccountType.CHECKING);
        accountDetails.setAccountNumber("123456789");
        accountDetails.setRoutingNumber("987654321");
        accountDetails.setInstitution("Bank");
        accountDetails.setInvestmentRate(BigDecimal.valueOf(0.05));
        accountDetails.setStartingBalance(BigDecimal.valueOf(1000));

        doReturn(0).when(accountService).updateAccount(eq(accountId), eq(userId), any(), anyString(), anyString(), anyString(), any(), any());

        mockMvc.perform(put("/accounts/{userId}/{id}", userId, accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"userId\":\"user123\",\"type\":\"CHECKING\",\"accountNumber\":\"123456789\"," +
                        "\"routingNumber\":\"987654321\",\"institution\":\"Bank\",\"investmentRate\":0.05," +
                        "\"startingBalance\":1000}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        String userId = "user123";
        int accountId = 1;
        doNothing().when(accountService).deleteAccount(accountId, userId);

        mockMvc.perform(delete("/accounts/{userId}/{id}", userId, accountId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(accountId, userId);
    }

    @Test
    public void testDeleteAllAccounts() throws Exception {
        String userId = "user123";

        doNothing().when(accountService).deleteAllAccounts(userId);

        mockMvc.perform(delete("/accounts/{userId}", userId))
                .andExpect(status().isNoContent());
    }
}