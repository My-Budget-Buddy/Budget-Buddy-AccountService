package com.skillstorm.budgetbuddyaccountservice.models;

import java.time.LocalDate;

public class Transaction {
    
    private int transactionId;
    private int userId;
    private int accountId;
    private String vendorName;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;
    
    public Transaction() {
    }

    public Transaction(int userId, int accountId, String vendorName, double amount, String category,
            String description, LocalDate date) {
        this.userId = userId;
        this.accountId = accountId;
        this.vendorName = vendorName;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Transaction(int transactionId, int userId, int accountId, String vendorName, double amount,
            String category, String description, LocalDate date) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.accountId = accountId;
        this.vendorName = vendorName;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + transactionId;
        result = prime * result + userId;
        result = prime * result + accountId;
        result = prime * result + ((vendorName == null) ? 0 : vendorName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (transactionId != other.transactionId)
            return false;
        if (userId != other.userId)
            return false;
        if (accountId != other.accountId)
            return false;
        if (vendorName == null) {
            if (other.vendorName != null)
                return false;
        } else if (!vendorName.equals(other.vendorName))
            return false;
        if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
            return false;
        if (category != other.category)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", userId=" + userId + ", accountId=" + accountId
                + ", vendorName=" + vendorName + ", amount=" + amount + ", category=" + category + ", description="
                + description + ", date=" + date + ", getTransactionId()=" + getTransactionId() + ", getClass()="
                + getClass() + ", getUserId()=" + getUserId() + ", getAccountId()=" + getAccountId()
                + ", getVendorName()=" + getVendorName() + ", getAmount()=" + getAmount() + ", getCategory()="
                + getCategory() + ", getDescription()=" + getDescription() + ", getDate()=" + getDate()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

    

    

    
}
