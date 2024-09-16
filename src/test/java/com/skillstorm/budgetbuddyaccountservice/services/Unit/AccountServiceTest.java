package com.skillstorm.budgetbuddyaccountservice.services.Unit;

//Dependency packages
import com.skillstorm.budgetbuddyaccountservice.models.Account;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.exceptions.AccountNotFoundException;
import com.skillstorm.budgetbuddyaccountservice.repositories.AccountRepository;
import com.skillstorm.budgetbuddyaccountservice.services.AccountService;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;

//Mockito
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

//Junit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

//Java utils
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.math.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccountsByUserId_Success() {
        // Arrange
        String userId = "user123";
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(userId,AccountType.CHECKING, "123456", "00001", "Bank A", new BigDecimal(5), new BigDecimal(1500));
        
        when(accountRepository.findByUserId(userId)).thenReturn(mockAccounts);

        // Act
        List<AccountDto> accountDtos = accountService.getAccountsByUserId(userId);

        // Assert
        assertNotNull(accountDtos);
        assertEquals(1, accountDtos.size());
        verify(accountRepository, times(1)).findByUserId(userId);
    }

    @Disabled
    @Test
    public void testSaveAccount_Success() {
        // Arrange
        AccountDto accountDto = new AccountDto("user123", AccountType.SAVINGS, "123456", "654321", "Bank B", BigDecimal.valueOf(2.5), BigDecimal.valueOf(1000), BigDecimal.valueOf(1500));
        Account account = new Account(1, "user123", "123456", "Bank B", BigDecimal.valueOf(2.5), BigDecimal.valueOf(1000));

        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        AccountDto result = accountService.saveAccount(accountDto);

        // Assert
        assertNotNull(result);
        verify(accountRepository, times(1)).save(account);
        verify(accountMapper, times(1)).toEntity(accountDto);
    }
    @Disabled
    @Test
    public void testGetAccountsByUserId_AccountNotFound() {
        // Arrange
        String userId = "user123";
        when(accountRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        // Act
        List<AccountDto> accountDtos = accountService.getAccountsByUserId(userId);

        // Assert
        assertTrue(accountDtos.isEmpty());
        verify(accountRepository, times(1)).findByUserId(userId);
    }
    @Disabled
    @Test
    public void testVerifyAccountOwnership_Success() {
        // Arrange
        String userId = "user123";
        int accountId = 1;
        Account account = new Account(1, userId, "123456", "Bank B", BigDecimal.valueOf(2.5), BigDecimal.valueOf(1000));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act & Assert
        assertDoesNotThrow(() -> accountService.verifyAccountOwnership(userId, accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }
    @Disabled
    @Test
    public void testVerifyAccountOwnership_AccountNotFound() {
        // Arrange
        String userId = "user123";
        int accountId = 1;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.verifyAccountOwnership(userId, accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }
}