package com.exceptions;

public class InvalidAccountDataException extends RuntimeException {
    public InvalidAccountDataException(String message) {
        super("Invalid account data: " + message);
    }
}