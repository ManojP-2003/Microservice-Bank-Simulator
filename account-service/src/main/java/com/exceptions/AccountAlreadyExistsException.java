package com.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String accountNumber) {
        super("Account already exists: " + accountNumber);
    }
}
