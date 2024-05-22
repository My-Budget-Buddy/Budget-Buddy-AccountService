package com.skillstorm.budgetbuddyaccountservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.mappers.AccountMapper;
import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;

public class AccountDtoTest {

    private Account account;
    private AccountDto accountDto;
    private AccountMapper accountMapper;

    @BeforeEach
    public void setup() {
        account = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        accountMapper = new AccountMapper();
        accountDto = accountMapper.toDto(account);
    }

   
    @Test
    public void testSetAndGetType() {
        accountDto.setType(AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, accountDto.getType(), "Type should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetInstitution() {
        accountDto.setInstitution("Bank B");
        assertEquals("Bank B", accountDto.getInstitution(), "Institution should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetInvestmentRate() {
        accountDto.setInvestmentRate(BigDecimal.valueOf(0.1));
        assertEquals(BigDecimal.valueOf(0.1), accountDto.getInvestmentRate(), "Investment rate should be set and retrieved correctly");
    }

    @Test
    public void testSetAndGetStartingBalance() {
        accountDto.setStartingBalance(BigDecimal.valueOf(2000));
        assertEquals(BigDecimal.valueOf(2000), accountDto.getStartingBalance(), "Starting balance should be set and retrieved correctly");
    }

    @Test
    public void testToAccount() {
        Account mappedAccount = accountMapper.toAccount(accountDto);

        assertEquals(account.getId(), mappedAccount.getId(), "Mapped Account ID should match");
        assertEquals(account.getUserId(), mappedAccount.getUserId(), "Mapped User ID should match");
        assertEquals(account.getType(), mappedAccount.getType(), "Mapped Account type should match");
        assertEquals(account.getAccountNumber(), mappedAccount.getAccountNumber(), "Mapped Account number should match");
        assertEquals(account.getRoutingNumber(), mappedAccount.getRoutingNumber(), "Mapped Routing number should match");
        assertEquals(account.getInstitution(), mappedAccount.getInstitution(), "Mapped Institution should match");
        assertEquals(account.getInvestmentRate(), mappedAccount.getInvestmentRate(), "Mapped Investment rate should match");
        assertEquals(account.getStartingBalance(), mappedAccount.getStartingBalance(), "Mapped Starting balance should match");
    }

    @Test
    public void testToDto() {
        AccountDto mappedAccountDto = accountMapper.toDto(account);

        assertEquals(accountDto.getId(), mappedAccountDto.getId(), "Mapped AccountDto ID should match");
        assertEquals(accountDto.getUserId(), mappedAccountDto.getUserId(), "Mapped User ID should match");
        assertEquals(accountDto.getType(), mappedAccountDto.getType(), "Mapped Account type should match");
        assertEquals(accountDto.getAccountNumber(), mappedAccountDto.getAccountNumber(), "Mapped Account number should match");
        assertEquals(accountDto.getRoutingNumber(), mappedAccountDto.getRoutingNumber(), "Mapped Routing number should match");
        assertEquals(accountDto.getInstitution(), mappedAccountDto.getInstitution(), "Mapped Institution should match");
        assertEquals(accountDto.getInvestmentRate(), mappedAccountDto.getInvestmentRate(), "Mapped Investment rate should match");
        assertEquals(accountDto.getStartingBalance(), mappedAccountDto.getStartingBalance(), "Mapped Starting balance should match");
        assertEquals(BigDecimal.ZERO, mappedAccountDto.getCurrentBalance(), "Mapped Additional field should match");
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(accountDto.equals(accountDto), "AccountDto should be equal to itself");
    }

    @Test
    public void testEqualsEqualObjects() {
        AccountDto anotherAccountDto = accountMapper.toDto(account);
        assertTrue(accountDto.equals(anotherAccountDto), "Equal objects should be considered equal");
        assertTrue(anotherAccountDto.equals(accountDto), "Equal objects should be considered equal");
    }

    @Test
    public void testEqualsDifferentObjects() {
        Account anotherAccount = new Account(2, "user2", Account.AccountType.SAVINGS, "987654321", "123456789",
                "Bank B", BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000));
        AccountDto differentAccountDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentAccountDto), "Different objects should not be considered equal");
        assertFalse(differentAccountDto.equals(accountDto), "Different objects should not be considered equal");
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(accountDto.equals(null), "AccountDto should not be equal to null");
    }

    @Test
    public void testEqualsDifferentClassObject() {
        Object obj = new Object();
        assertFalse(accountDto.equals(obj), "AccountDto should not be equal to an object of a different class");
    }

    @Test
    public void testHashCodeConsistency() {
        AccountDto anotherAccountDto = accountMapper.toDto(account);
        assertEquals(accountDto.hashCode(), anotherAccountDto.hashCode(),
                "Hash codes should be equal for equal objects");
    }

    @Test
    public void testHashCodeUniqueness() {
        Account anotherAccount = new Account(2, "user2", Account.AccountType.SAVINGS, "987654321", "123456789",
                "Bank B", BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000));
        AccountDto differentAccountDto = accountMapper.toDto(anotherAccount);

        assertNotEquals(accountDto.hashCode(), differentAccountDto.hashCode(),
                "Hash codes should be different for different objects");
    }

    @Test
    public void testAccountDtoConstructor() {
        AccountDto accountDto = new AccountDto("user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertEquals("user1", accountDto.getUserId(), "User ID should match");
        assertEquals(AccountType.CHECKING, accountDto.getType(), "Account type should match");
        assertEquals("123456789", accountDto.getAccountNumber(), "Account number should match");
        assertEquals("987654321", accountDto.getRoutingNumber(), "Routing number should match");
        assertEquals("Bank A", accountDto.getInstitution(), "Institution should match");
        assertEquals(BigDecimal.valueOf(0.05), accountDto.getInvestmentRate(), "Investment rate should match");
        assertEquals(BigDecimal.valueOf(1000), accountDto.getStartingBalance(), "Starting balance should match");
        assertEquals(BigDecimal.valueOf(1000), accountDto.getCurrentBalance(), "Current balance should match");
    }

    @Test
    public void testAccountDtoConstructorNullValues() {
        AccountDto accountDto = new AccountDto(null, null, null, null,
                null, null, null, null);

        assertNull(accountDto.getUserId(), "User ID should be null");
        assertNull(accountDto.getType(), "Account type should be null");
        assertNull(accountDto.getAccountNumber(), "Account number should be null");
        assertNull(accountDto.getRoutingNumber(), "Routing number should be null");
        assertNull(accountDto.getInstitution(), "Institution should be null");
        assertNull(accountDto.getInvestmentRate(), "Investment rate should be null");
        assertNull(accountDto.getStartingBalance(), "Starting balance should be null");
        assertNull(accountDto.getCurrentBalance(), "Current balance should be null");
    }

    
    @Test
    public void testEqualsDifferentUserId() {
        Account anotherAccount = new Account(2, "user2", Account.AccountType.SAVINGS, "123456789", "987654321",
                "Bank B", BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000));
        AccountDto differentUserIdDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentUserIdDto), "Different user IDs should result in non-equal AccountDto");
        assertFalse(differentUserIdDto.equals(accountDto), "Different user IDs should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentAccountType() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.SAVINGS, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        AccountDto differentAccountTypeDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentAccountTypeDto), "Different account types should result in non-equal AccountDto");
        assertFalse(differentAccountTypeDto.equals(accountDto), "Different account types should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentStartingBalance() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(2000));
        AccountDto differentStartingBalanceDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentStartingBalanceDto), "Different starting balances should result in non-equal AccountDto");
        assertFalse(differentStartingBalanceDto.equals(accountDto), "Different starting balances should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentAccountNumber() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.CHECKING, "987654321", "123456789",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        AccountDto differentAccountNumberDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentAccountNumberDto), "Different account numbers should result in non-equal AccountDto");
        assertFalse(differentAccountNumberDto.equals(accountDto), "Different account numbers should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentRoutingNumber() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.CHECKING, "123456789", "9876543210",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        AccountDto differentRoutingNumberDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentRoutingNumberDto), "Different routing numbers should result in non-equal AccountDto");
        assertFalse(differentRoutingNumberDto.equals(accountDto), "Different routing numbers should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentInstitution() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank B", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        AccountDto differentInstitutionDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentInstitutionDto), "Different institutions should result in non-equal AccountDto");
        assertFalse(differentInstitutionDto.equals(accountDto), "Different institutions should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentInvestmentRate() {
        Account anotherAccount = new Account(2, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000));
        AccountDto differentInvestmentRateDto = accountMapper.toDto(anotherAccount);

        assertFalse(accountDto.equals(differentInvestmentRateDto), "Different investment rates should result in non-equal AccountDto");
        assertFalse(differentInvestmentRateDto.equals(accountDto), "Different investment rates should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentInvestmentRateNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", null, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different investment rates (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different investment rates (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentStartingBalanceNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), null, BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different starting balances (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different starting balances (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentCurrentBalanceNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), null);
        AccountDto accountDto2 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different current balances (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different current balances (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentTypeNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(1, "user1", null, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different account types (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different account types (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentInstitutionNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                null, BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different institutions (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different institutions (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentRoutingNumberNull() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", null,
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different routing numbers (one null) should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different routing numbers (one null) should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentId() {
        AccountDto accountDto1 = new AccountDto(1, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountDto accountDto2 = new AccountDto(2, "user1", AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));

        assertFalse(accountDto1.equals(accountDto2), "Different IDs should result in non-equal AccountDto");
        assertFalse(accountDto2.equals(accountDto1), "Different IDs should result in non-equal AccountDto");
    }

    @Test
    public void testEqualsDifferentObject() {
        assertFalse(accountDto.equals(new Object()), "AccountDto should not be equal to an object of a different class");
    }

    @Test
    public void testEqualsSameObjectReference() {
        assertTrue(accountDto.equals(accountDto), "AccountDto should be equal to itself");
    }
    

}
