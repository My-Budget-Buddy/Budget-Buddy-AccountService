package com.skillstorm.budgetbuddyaccountservice.exceptions;

public class NotEnoughInformationException extends RuntimeException {

    public NotEnoughInformationException(String error) {
        super("NotEnoughInformationException: " + error);
    }

}
