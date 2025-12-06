package com.exceptions;

public class TransactionFailedException extends RuntimeException {
    public TransactionFailedException(String message) {
        super("Transaction failed: " + message);
    }
}