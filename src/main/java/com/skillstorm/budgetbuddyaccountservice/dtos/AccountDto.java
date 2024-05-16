package com.skillstorm.budgetbuddyaccountservice.dtos;

import java.math.BigDecimal;

import com.skillstorm.budgetbuddyaccountservice.models.Account.AccountType;

public class AccountDto {

    private int id;
    private String userId;
    private AccountType type;
    private String accountNumber;
    private String routingNumber;
    private String institution;
    private BigDecimal investmentRate;
    private BigDecimal startingBalance;
    private BigDecimal currentBalance;

    public AccountDto() {
    }

    public AccountDto(String userId, AccountType type, String accountNumber, String routingNumber, String institution,
            BigDecimal investmentRate, BigDecimal startingBalance, BigDecimal currentBalance) {
        this.userId = userId;
        this.type = type;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.institution = institution;
        this.investmentRate = investmentRate;
        this.startingBalance = startingBalance;
        this.currentBalance = currentBalance;
    }

    public AccountDto(int id, String userId, AccountType type, String accountNumber, String routingNumber,
            String institution, BigDecimal investmentRate, BigDecimal startingBalance, BigDecimal currentBalance) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.institution = institution;
        this.investmentRate = investmentRate;
        this.startingBalance = startingBalance;
        this.currentBalance = currentBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public BigDecimal getInvestmentRate() {
        return investmentRate;
    }

    public void setInvestmentRate(BigDecimal investmentRate) {
        this.investmentRate = investmentRate;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
        result = prime * result + ((institution == null) ? 0 : institution.hashCode());
        result = prime * result + ((investmentRate == null) ? 0 : investmentRate.hashCode());
        result = prime * result + ((startingBalance == null) ? 0 : startingBalance.hashCode());
        result = prime * result + ((currentBalance == null) ? 0 : currentBalance.hashCode());
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
        AccountDto other = (AccountDto) obj;
        if (id != other.id)
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (type != other.type)
            return false;
        if (accountNumber == null) {
            if (other.accountNumber != null)
                return false;
        } else if (!accountNumber.equals(other.accountNumber))
            return false;
        if (routingNumber == null) {
            if (other.routingNumber != null)
                return false;
        } else if (!routingNumber.equals(other.routingNumber))
            return false;
        if (institution == null) {
            if (other.institution != null)
                return false;
        } else if (!institution.equals(other.institution))
            return false;
        if (investmentRate == null) {
            if (other.investmentRate != null)
                return false;
        } else if (!investmentRate.equals(other.investmentRate))
            return false;
        if (startingBalance == null) {
            if (other.startingBalance != null)
                return false;
        } else if (!startingBalance.equals(other.startingBalance))
            return false;
        if (currentBalance == null) {
            if (other.currentBalance != null)
                return false;
        } else if (!currentBalance.equals(other.currentBalance))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AccountDto [id=" + id + ", userId=" + userId + ", type=" + type + ", accountNumber=" + accountNumber
                + ", routingNumber=" + routingNumber + ", institution=" + institution + ", investmentRate="
                + investmentRate + ", startingBalance=" + startingBalance + ", currentBalance=" + currentBalance + "]";
    }

}
