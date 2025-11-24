package com.service;

import com.model.Account;
import com.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository repo;

    public AccountServiceImpl(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Account createAccount(Account account) {
        account.setCreatedAt(new Date());
        account.setStatus("ACTIVE");
        return repo.save(account);
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return repo.findByAccountNumber(accountNumber);
    }

    @Override
    public Account updateBalance(String accountNumber, double balance) {
        Account acc = repo.findByAccountNumber(accountNumber);
        if (acc == null) return null;
        acc.setBalance(balance);
        return repo.save(acc);
    }

    @Override
    public Account updateStatus(String accountNumber, String status) {
        Account acc = repo.findByAccountNumber(accountNumber);
        if (acc == null) return null;
        acc.setStatus(status);
        return repo.save(acc);
    }

    @Override
    public boolean deleteAccount(String accountNumber) {
        Account acc = repo.findByAccountNumber(accountNumber);
        if (acc == null) return false;
        repo.delete(acc);
        return true;
    }
}
