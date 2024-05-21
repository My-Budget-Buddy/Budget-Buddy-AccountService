package com.skillstorm.budgetbuddyaccountservice.models;

public enum TransactionCategory {
    GROCERIES,
    ENTERTAINMENT,
    DINING,
    TRANSPORTATION,
    HEALTHCARE,
    LIVING_EXPENSES,
    SHOPPING,
    INCOME,
    MISC;

    @Override
    public String toString() {
        // Replace underscores with spaces and capitalize each word
        String name = name().replace('_', ' ').toLowerCase();
        String[] words = name.split(" ");
        StringBuilder displayName = new StringBuilder();
        for (String word : words) {
            displayName.append(Character.toUpperCase(word.charAt(0)))
                       .append(word.substring(1))
                       .append(" ");
        }
        return displayName.toString().trim();
    }
}
