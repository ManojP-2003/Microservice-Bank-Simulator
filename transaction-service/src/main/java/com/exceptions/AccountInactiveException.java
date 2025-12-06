package com.exceptions;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException(String message) {
        super("Account inactive: " + message);
    }
}
