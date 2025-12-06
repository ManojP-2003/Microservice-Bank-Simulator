package com.service;

import com.model.Account;
import com.repository.AccountRepository;
import com.exceptions.*;
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

    private String generateUniqueAccountNumber(String holderName) {

        if (holderName == null || holderName.trim().isEmpty()) {
            throw new InvalidAccountDataException("Holder name cannot be empty");
        }

        String initials = holderName.replaceAll("[^A-Za-z]", "");

        if (initials.length() < 2) {
            throw new InvalidAccountDataException("Holder name must contain at least 2 letters");
        }

        initials = initials.length() >= 3
                ? initials.substring(0, 3).toUpperCase()
                : initials.toUpperCase();

        String accountNumber;
        do {
            int rand = (int) (Math.random() * 9000) + 1000;
            accountNumber = initials + rand;
        } while (repo.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }


    @Override
    public Account createAccount(Account account) {

        logger.info("Creating account for: {}", account.getHolderName());

        if (account.getHolderName() == null || account.getHolderName().trim().isEmpty()) {
            throw new InvalidAccountDataException("Holder name is required");
        }

        if (account.getBalance() < 0) {
            throw new InvalidAccountDataException("Initial balance cannot be negative");
        }

        if (account.getAccountNumber() != null && !account.getAccountNumber().isEmpty()) {
            Account existing = repo.findByAccountNumber(account.getAccountNumber());
            if (existing != null) {
                throw new AccountAlreadyExistsException("Account already exists: " + account.getAccountNumber());
            }
        }

        String generated = generateUniqueAccountNumber(account.getHolderName());
        account.setAccountNumber(generated);
        account.setCreatedAt(new Date());
        account.setStatus("ACTIVE");

        return repo.save(account);
    }


    @Override
    public Account getAccountByNumber(String accountNumber) {

        logger.info("Fetching account: {}", accountNumber);

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InvalidAccountDataException("Account number cannot be empty");
        }

        Account account = repo.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }

        return account;
    }


    @Override
    public Account updateBalance(String accountNumber, double amount) {

        logger.info("Updating balance for account: {} by {}", accountNumber, amount);

        Account acc = repo.findByAccountNumber(accountNumber);

        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }

        if (!"ACTIVE".equals(acc.getStatus())) {
            throw new AccountInactiveException("Account is inactive: " + accountNumber);
        }

        double newBalance = acc.getBalance() + amount;

        if (newBalance < 0) {
            throw new InsufficientBalanceException("Insufficient balance: " + accountNumber);
        }

        acc.setBalance(newBalance);
        return repo.save(acc);
    }


    @Override
    public Account updateStatus(String accountNumber, String status) {

        logger.info("Updating status for {} to {}", accountNumber, status);

        if (!"ACTIVE".equals(status) && !"INACTIVE".equals(status)) {
            throw new InvalidAccountDataException("Invalid status: " + status);
        }

        Account acc = repo.findByAccountNumber(accountNumber);

        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }

        acc.setStatus(status);
        return repo.save(acc);
    }


    @Override
    public boolean deleteAccount(String accountNumber) {

        logger.info("Deleting account: {}", accountNumber);

        Account acc = repo.findByAccountNumber(accountNumber);

        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }

        if (acc.getBalance() > 0) {
            throw new InvalidAccountDataException("Cannot delete account with positive balance: " + accountNumber);
        }

        repo.delete(acc);
        return true;
    }
}
