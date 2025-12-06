package com.exceptions;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException(String accountNumber) {
        super("Account is inactive: " + accountNumber);
    }
}
