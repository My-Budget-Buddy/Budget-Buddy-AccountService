package com.skillstorm.budgetbuddyaccountservice.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
import com.skillstorm.budgetbuddyaccountservice.models.Account;

@Component
public class AccountMapper {

    public Account toAccount(AccountDto dto) {
        return new Account(dto.getId(), dto.getUserId(), dto.getType(), dto.getAccountNumber(), dto.getRoutingNumber(),
                dto.getInstitution(), dto.getInvestmentRate(), dto.getStartingBalance());
    }

    public AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getUserId(), account.getType(), account.getAccountNumber(),
                account.getRoutingNumber(), account.getInstitution(), account.getInvestmentRate(),
                account.getStartingBalance(), new BigDecimal(0));
    }

}
