package com.service;

import com.model.Account;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountByNumber(String accountNumber);

    Account updateBalance(String accountNumber, double amount);

    Account updateStatus(String accountNumber, String status);

    boolean deleteAccount(String accountNumber);
}
