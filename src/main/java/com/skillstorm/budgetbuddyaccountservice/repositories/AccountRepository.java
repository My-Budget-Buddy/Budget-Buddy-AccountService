package com.skillstorm.budgetbuddyaccountservice.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.budgetbuddyaccountservice.models.Account;

import jakarta.transaction.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.userId = :userId, a.type = :_type, a.accountNumber = :accountNumber, "
            + "a.routingNumber = :routingNumber, a.institution = :institution, "
            + "a.investmentRate = :investmentRate, a.startingBalance = :startingBalance "
            + "WHERE a.id = :id")
    int updateAccount(@Param("id") int id, @Param("userId") String userId, @Param("_type") Account.AccountType type,
                      @Param("accountNumber") String accountNumber, @Param("routingNumber") String routingNumber,
                      @Param("institution") String institution, @Param("investmentRate") BigDecimal investmentRate,
                      @Param("startingBalance") BigDecimal startingBalance);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    List<Account> findByUserId(@Param("userId") String userId);
}
