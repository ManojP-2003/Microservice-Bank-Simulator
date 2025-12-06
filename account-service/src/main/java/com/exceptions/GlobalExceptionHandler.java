package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Object> handleAccountAlreadyExists(AccountAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), "ACCOUNT_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidAccountDataException.class)
    public ResponseEntity<Object> handleInvalidAccountData(InvalidAccountDataException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), "INVALID_ACCOUNT_DATA", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<Object> handleInactive(AccountInactiveException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), "ACCOUNT_INACTIVE", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Object> handleInsufficientBalance(InsufficientBalanceException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), "INSUFFICIENT_BALANCE", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return createErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> createErrorResponse(String message, String errorCode, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("errorCode", errorCode);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}
