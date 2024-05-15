package com.skillstorm.budgetbuddyaccountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.budgetbuddyaccountservice.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
