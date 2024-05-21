package com.skillstorm.budgetbuddyaccountservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class AccountTest {
    
    @Test
    void testEqualsAndHashCode() {
        Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
        Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

        // Test for equality
        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());

        // Change one attribute to make them unequal
        account2.setUserId("user2");

        // Test for inequality
        assertNotEquals(account1, account2);
        assertNotEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void testToString() {
        Account account = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321", "Bank A",
                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

        String expectedToString = "Account [id=1, userId=user1, type=CHECKING, accountNumber=123456789, " +
                "routingNumber=987654321, institution=Bank A, investmentRate=0.05, startingBalance=1000]";

        assertEquals(expectedToString, account.toString());
    }
}
