package com.skillstorm.budgetbuddyaccountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotEnoughInformationException.class)
    public ResponseEntity<String> notEnoughInformationException(NotEnoughInformationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(IdMismatchException.class)
    public ResponseEntity<String> idMismatchException(IdMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

}
