package com.skillstorm.budgetbuddyaccountservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class GlobalExcpetionHandlerTest {
    
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

   
    @Test
    public void testNotEnoughInformationException() {
        NotEnoughInformationException exception = new NotEnoughInformationException("Not enough information provided");

        ResponseEntity<String> expectedResponse = new ResponseEntity<>("NotEnoughInformationException: Not enough information provided", HttpStatus.EXPECTATION_FAILED);

        ResponseEntity<String> actualResponse = globalExceptionHandler.notEnoughInformationException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }
}
