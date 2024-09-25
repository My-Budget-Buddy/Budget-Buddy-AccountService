package com.skillstorm.budgetbuddyaccountservice.models;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;
import com.skillstorm.budgetbuddyaccountservice.dtos.AccountDto;
public class AllModelAndDto {

    @Test
    public void meanBeanTests(){
        BeanVerifier.verifyBeans( Account.class, Transaction.class, AccountDto.class);
    }

}
