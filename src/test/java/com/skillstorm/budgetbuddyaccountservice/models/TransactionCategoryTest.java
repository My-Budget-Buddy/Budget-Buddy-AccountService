package com.skillstorm.budgetbuddyaccountservice.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TransactionCategoryTest {
    
      @Test
    public void testTransactionCategoryEnumValues() {
        TransactionCategory[] expectedValues = {
            TransactionCategory.GROCERIES,
            TransactionCategory.ENTERTAINMENT,
            TransactionCategory.DINING,
            TransactionCategory.TRANSPORTATION,
            TransactionCategory.HEALTHCARE,
            TransactionCategory.LIVING_EXPENSES,
            TransactionCategory.SHOPPING,
            TransactionCategory.INCOME,
            TransactionCategory.MISC
        };
        
        assertArrayEquals(expectedValues, TransactionCategory.values());
    }

    @Test
    public void testTransactionCategoryToString() {
        assertEquals("Groceries", TransactionCategory.GROCERIES.toString());
        assertEquals("Entertainment", TransactionCategory.ENTERTAINMENT.toString());
        assertEquals("Dining", TransactionCategory.DINING.toString());
        assertEquals("Transportation", TransactionCategory.TRANSPORTATION.toString());
        assertEquals("Healthcare", TransactionCategory.HEALTHCARE.toString());
        assertEquals("Living Expenses", TransactionCategory.LIVING_EXPENSES.toString());
        assertEquals("Shopping", TransactionCategory.SHOPPING.toString());
        assertEquals("Income", TransactionCategory.INCOME.toString());
        assertEquals("Misc", TransactionCategory.MISC.toString());
    }
}
