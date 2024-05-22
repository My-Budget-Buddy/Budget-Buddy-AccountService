package com.skillstorm.budgetbuddyaccountservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transaction = new Transaction();
    }

    @Test
    public void testSetAndGetTransactionId() {
        int transactionId = 1;
        transaction.setTransactionId(transactionId);
        assertEquals(transactionId, transaction.getTransactionId());
    }

    @Test
    public void testSetAndGetAccountId() {
        int accountId = 1;
        transaction.setAccountId(accountId);
        assertEquals(accountId, transaction.getAccountId());
    }

    @Test
    public void testSetAndGetUserId() {
        int userId = 1;
        transaction.setUserId(userId);
        assertEquals(userId, transaction.getUserId());
    }

    @Test
    public void testSetAndGetVendorName() {
        String vendorName = "Vendor";
        transaction.setVendorName(vendorName);
        assertEquals(vendorName, transaction.getVendorName());
    }

    @Test
    public void testSetAndGetAmount() {
        double amount = 100.00;
        transaction.setAmount(amount);
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    public void testSetAndGetCategory() {
        String category = "Shopping";
        transaction.setCategory(category);
        assertEquals(category, transaction.getCategory());
    }

    @Test
    public void testSetAndGetDate() {
        LocalDate date = LocalDate.now();
        transaction.setDate(date);
        assertEquals(date, transaction.getDate());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Sample Description";
        transaction.setDescription(description);
        assertEquals(description, transaction.getDescription());
    }
    @Test
    public void testFirstConstructor() {
        int userId = 1;
        int accountId = 123;
        String vendorName = "Vendor";
        double amount = 100.0;
        String category = "Category";
        String description = "Description";
        LocalDate date = LocalDate.now();

        Transaction transaction = new Transaction(userId, accountId, vendorName, amount, category, description, date);

        assertEquals(userId, transaction.getUserId(), "User ID should be initialized correctly");
        assertEquals(accountId, transaction.getAccountId(), "Account ID should be initialized correctly");
        assertEquals(vendorName, transaction.getVendorName(), "Vendor name should be initialized correctly");
        assertEquals(amount, transaction.getAmount(), 0.01, "Amount should be initialized correctly");
        assertEquals(category, transaction.getCategory(), "Category should be initialized correctly");
        assertEquals(description, transaction.getDescription(), "Description should be initialized correctly");
        assertEquals(date, transaction.getDate(), "Date should be initialized correctly");
    }

    @Test
    public void testSecondConstructor() {
        int transactionId = 1234;
        int userId = 1;
        int accountId = 123;
        String vendorName = "Vendor";
        double amount = 100.0;
        String category = "Category";
        String description = "Description";
        LocalDate date = LocalDate.now();

        Transaction transaction = new Transaction(transactionId, userId, accountId, vendorName, amount, category,
                description, date);

        assertEquals(transactionId, transaction.getTransactionId(), "Transaction ID should be initialized correctly");
        assertEquals(userId, transaction.getUserId(), "User ID should be initialized correctly");
        assertEquals(accountId, transaction.getAccountId(), "Account ID should be initialized correctly");
        assertEquals(vendorName, transaction.getVendorName(), "Vendor name should be initialized correctly");
        assertEquals(amount, transaction.getAmount(), 0.01, "Amount should be initialized correctly");
        assertEquals(category, transaction.getCategory(), "Category should be initialized correctly");
        assertEquals(description, transaction.getDescription(), "Description should be initialized correctly");
        assertEquals(date, transaction.getDate(), "Date should be initialized correctly");
    }

    @Test
    public void testHashCode() {
        int transactionId = 1234;
        int userId = 1;
        int accountId = 123;
        String vendorName = "Vendor";
        double amount = 100.0;
        String category = "Category";
        String description = "Description";
        LocalDate date = LocalDate.now();

        Transaction transaction = new Transaction(transactionId, userId, accountId, vendorName, amount, category,
                description, date);

        final int prime = 31;
        int expectedHashCode = 1;
        expectedHashCode = prime * expectedHashCode + transactionId;
        expectedHashCode = prime * expectedHashCode + userId;
        expectedHashCode = prime * expectedHashCode + accountId;
        expectedHashCode = prime * expectedHashCode + ((vendorName == null) ? 0 : vendorName.hashCode());
        long temp = Double.doubleToLongBits(amount);
        expectedHashCode = prime * expectedHashCode + (int) (temp ^ (temp >>> 32));
        expectedHashCode = prime * expectedHashCode + ((category == null) ? 0 : category.hashCode());
        expectedHashCode = prime * expectedHashCode + ((description == null) ? 0 : description.hashCode());
        expectedHashCode = prime * expectedHashCode + ((date == null) ? 0 : date.hashCode());

        assertEquals(expectedHashCode, transaction.hashCode(), "Hash code should be calculated correctly");
    }

     @Test
    public void testEqualsSameObject() {
        Transaction transaction = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description", LocalDate.now());

        assertTrue(transaction.equals(transaction), "Transaction should be equal to itself");
    }

    @Test
    public void testEqualsEqualObjects() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description", LocalDate.now());
        Transaction transaction2 = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description", LocalDate.now());

        assertTrue(transaction1.equals(transaction2), "Equal objects should be considered equal");
        assertTrue(transaction2.equals(transaction1), "Equal objects should be considered equal");
    }

    @Test
    public void testEqualsDifferentObjects() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor1", 100.0, "Category1", "Description1",
                LocalDate.now());
        Transaction transaction2 = new Transaction(2, 456, "Vendor2", 200.0, "Category2", "Description2",
                LocalDate.now());

        assertFalse(transaction1.equals(transaction2), "Different objects should not be considered equal");
        assertFalse(transaction2.equals(transaction1), "Different objects should not be considered equal");
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(transaction.equals(null), "Transaction should not be equal to null");
    }

    @Test
    public void testEqualsDifferentClassObject() {
        Object obj = new Object();
        assertFalse(transaction.equals(obj), "Transaction should not be equal to an object of a different class");
    }

    // Additional tests for branch coverage

    @Test
    public void testEqualsDifferentVendorName() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor1", 100.0, "Category", "Description", LocalDate.now());
        Transaction transaction2 = new Transaction(1, 123, "Vendor2", 100.0, "Category", "Description", LocalDate.now());

        assertFalse(transaction1.equals(transaction2), "Transactions with different vendor names should not be considered equal");
        assertFalse(transaction2.equals(transaction1), "Transactions with different vendor names should not be considered equal");
    }

    @Test
    public void testEqualsDifferentAmount() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description", LocalDate.now());
        Transaction transaction2 = new Transaction(1, 123, "Vendor", 200.0, "Category", "Description", LocalDate.now());

        assertFalse(transaction1.equals(transaction2), "Transactions with different amounts should not be considered equal");
        assertFalse(transaction2.equals(transaction1), "Transactions with different amounts should not be considered equal");
    }

    @Test
    public void testEqualsDifferentCategory() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor", 100.0, "Category1", "Description", LocalDate.now());
        Transaction transaction2 = new Transaction(1, 123, "Vendor", 100.0, "Category2", "Description", LocalDate.now());

        assertFalse(transaction1.equals(transaction2), "Transactions with different categories should not be considered equal");
        assertFalse(transaction2.equals(transaction1), "Transactions with different categories should not be considered equal");
    }

    @Test
    public void testEqualsDifferentDescription() {
        Transaction transaction1 = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description1", LocalDate.now());
        Transaction transaction2 = new Transaction(1, 123, "Vendor", 100.0, "Category", "Description2", LocalDate.now());

        assertFalse(transaction1.equals(transaction2), "Transactions with different descriptions should not be considered equal");
        assertFalse(transaction2.equals(transaction1), "Transactions with different descriptions should not be considered equal");
    }

   
}
