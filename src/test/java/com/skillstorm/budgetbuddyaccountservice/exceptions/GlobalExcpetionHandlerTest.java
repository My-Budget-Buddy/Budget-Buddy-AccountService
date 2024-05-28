package com.skillstorm.budgetbuddyaccountservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExcpetionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testNotEnoughInformationException() {
        NotEnoughInformationException exception = new NotEnoughInformationException("Not enough information provided");

        ResponseEntity<String> expectedResponse = new ResponseEntity<>(
                "NotEnoughInformationException: Not enough information provided", HttpStatus.EXPECTATION_FAILED);

        ResponseEntity<String> actualResponse = globalExceptionHandler.notEnoughInformationException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }

    @Test
    public void testIdMismatchException() {
        IdMismatchException exception = new IdMismatchException();

        ResponseEntity<String> expectedResponse = new ResponseEntity<>(
                "Your account ID does not match the ID of the requested account",
                HttpStatus.FORBIDDEN);

        ResponseEntity<String> actualResponse = globalExceptionHandler.idMismatchException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }

    @Test
    public void testAccountNotFoundException() {
        String errorMessage = "Account not found.";
        AccountNotFoundException exception = new AccountNotFoundException(errorMessage);

        ResponseEntity<String> expectedResponse = new ResponseEntity<>(
                errorMessage,
                HttpStatus.NOT_FOUND);

        ResponseEntity<String> actualResponse = globalExceptionHandler.accountNotFoundException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Custom error message";
        AccountNotFoundException exception = new AccountNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage(), "Error message should match the one provided in the constructor");
    }
}
