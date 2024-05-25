package com.skillstorm.budgetbuddyaccountservice.exceptions;

public class InvalidGatewaySecretException extends RuntimeException {
    public InvalidGatewaySecretException() {
        super("Invalid gateway secret.");
    }

    public InvalidGatewaySecretException(String message) {
        super(message);
    }
}
