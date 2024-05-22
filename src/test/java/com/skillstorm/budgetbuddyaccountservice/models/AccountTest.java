package com.skillstorm.budgetbuddyaccountservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;

public class AccountTest {

        private Account account;

        @BeforeEach
        public void setup() {
                account = new Account();
        }

        @Test
        void testEqualsAndHashCode() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertEquals(account1, account2);
                assertEquals(account1.hashCode(), account2.hashCode());

                account2.setUserId("user2");

                assertNotEquals(account1, account2);
                assertNotEquals(account1.hashCode(), account2.hashCode());
        }

        @Test
        void testToString() {
                Account account = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                String expectedToString = "Account [id=1, userId=user1, type=CHECKING, accountNumber=123456789, " +
                                "routingNumber=987654321, institution=Bank A, investmentRate=0.05, startingBalance=1000]";

                assertEquals(expectedToString, account.toString());
        }

        @Test
        public void testHashCodeConsistency() {
                Account account1 = new Account(1, "user1", AccountType.CHECKING, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));
                Account account2 = new Account(1, "user1", AccountType.CHECKING, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));

                assertEquals(account1.hashCode(), account2.hashCode(), "Hash codes should be equal for equal objects");
        }

        @Test
        public void testHashCodeUniqueness() {
                Account account1 = new Account(1, "user1", AccountType.SAVINGS, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));
                Account account2 = new Account(2, "user2", AccountType.CHECKING, "654321", "210987", "institution2",
                                BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000.0));

                assertNotEquals(account1.hashCode(), account2.hashCode(),
                                "Hash codes should be different for different objects");
        }

        @Test
        public void testEqualsSameObject() {
                Account account1 = new Account(1, "user1", AccountType.SAVINGS, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));

                assertTrue(account1.equals(account1), "Account should be equal to itself");
        }

        @Test
        public void testEqualsEqualObjects() {
                Account account1 = new Account(1, "user1", AccountType.SAVINGS, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));
                Account account2 = new Account(1, "user1", AccountType.SAVINGS, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));

                assertTrue(account1.equals(account2), "Equal objects should be considered equal");
                assertTrue(account2.equals(account1), "Equal objects should be considered equal");
        }

        @Test
        public void testEqualsDifferentObjects() {
                Account account1 = new Account(1, "user1", AccountType.SAVINGS, "123456", "789012", "institution1",
                                BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000.0));
                Account account2 = new Account(2, "user2", AccountType.CHECKING, "654321", "210987", "institution2",
                                BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000.0));

                assertFalse(account1.equals(account2), "Different objects should not be considered equal");
                assertFalse(account2.equals(account1), "Different objects should not be considered equal");
        }

        @Test
        public void testEqualsNullObject() {
                assertFalse(account.equals(null), "Account should not be equal to null");
        }

        @Test
        public void testEqualsDifferentClassObject() {
                Object obj = new Object();
                assertFalse(account.equals(obj), "Account should not be equal to an object of a different class");
        }

        @Test
        public void testEqualsDifferentUserId() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user2", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2),
                                "Accounts with different user IDs should not be considered equal");
                assertFalse(account2.equals(account1),
                                "Accounts with different user IDs should not be considered equal");
        }

        @Test
        public void testEqualsDifferentAccountType() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.SAVINGS, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2), "Accounts with different types should not be considered equal");
                assertFalse(account2.equals(account1), "Accounts with different types should not be considered equal");
        }

        @Test
        public void testEqualsDifferentAccountNumber() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "987654321", "123456789",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2),
                                "Accounts with different account numbers should not be considered equal");
                assertFalse(account2.equals(account1),
                                "Accounts with different account numbers should not be considered equal");
        }

        @Test
        public void testEqualsDifferentRoutingNumber() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "123456789",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2),
                                "Accounts with different routing numbers should not be considered equal");
                assertFalse(account2.equals(account1),
                                "Accounts with different routing numbers should not be considered equal");
        }

        @Test
        public void testEqualsDifferentInstitution() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank B", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2),
                                "Accounts with different institutions should not be considered equal");
                assertFalse(account2.equals(account1),
                                "Accounts with different institutions should not be considered equal");
        }

        @Test
        public void testEqualsSameValues() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertTrue(account1.equals(account2), "Accounts with the same values should be considered equal");
                assertTrue(account2.equals(account1), "Accounts with the same values should be considered equal");
        }

        @Test
        public void testEqualsDifferentStartingBalance() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(2000));

                assertFalse(account1.equals(account2),
                                "Accounts with different starting balances should not be considered equal");
                assertFalse(account2.equals(account1),
                                "Accounts with different starting balances should not be considered equal");
        }

        @Test
        public void testEqualsDifferentClass() {
                Account account = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                assertFalse(account.equals("not an Account object"),
                                "An account should not be equal to a different class");
        }

        @Test
        public void testEqualsDifferentId() {
                Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
                Account account2 = new Account(2, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                                "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));

                assertFalse(account1.equals(account2), "Accounts with different IDs should not be considered equal");
                assertFalse(account2.equals(account1), "Accounts with different IDs should not be considered equal");
        }

        @Test
        public void testEqualsDifferentInvestmentRate() {
            Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
            Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000));
    
            assertFalse(account1.equals(account2),
                    "Accounts with different investment rates should not be considered equal");
            assertFalse(account2.equals(account1),
                    "Accounts with different investment rates should not be considered equal");
        }
    
        @Test
        public void testEqualsDifferentUserIdNull() {
            Account account1 = new Account(1, null, Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
            Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
    
            assertFalse(account1.equals(account2),
                    "Accounts with different user IDs (one null) should not be considered equal");
            assertFalse(account2.equals(account1),
                    "Accounts with different user IDs (one null) should not be considered equal");
        }

        @Test
        public void testEqualsDifferentStartingBalanceNull() {
            Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), null);
            Account account2 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
    
            assertFalse(account1.equals(account2),
                    "Accounts with different starting balances (one null) should not be considered equal");
            assertFalse(account2.equals(account1),
                    "Accounts with different starting balances (one null) should not be considered equal");
        }
    
        @Test
        public void testEqualsDifferentAccountTypeNull() {
            Account account1 = new Account(1, "user1", Account.AccountType.CHECKING, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
            Account account2 = new Account(1, "user1", null, "123456789", "987654321",
                    "Bank A", BigDecimal.valueOf(0.05), BigDecimal.valueOf(1000));
    
            assertFalse(account1.equals(account2),
                    "Accounts with different account types (one null) should not be considered equal");
            assertFalse(account2.equals(account1),
                    "Accounts with different account types (one null) should not be considered equal");
        }

}
