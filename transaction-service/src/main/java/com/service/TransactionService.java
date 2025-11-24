package com.service;

import com.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction deposit(String accountNumber, double amount);

    Transaction withdraw(String accountNumber, double amount);

    Transaction transfer(String fromAccount, String toAccount, double amount);

    List<Transaction> getTransactions(String accountNumber);
}

