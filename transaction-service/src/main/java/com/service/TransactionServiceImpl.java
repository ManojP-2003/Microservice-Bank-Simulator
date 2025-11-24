package com.service;

import com.client.AccountClient;
import com.client.NotificationClient;
import com.model.Transaction;
import com.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository repo;
    private final AccountClient accountClient;
    private final NotificationClient notificationClient;

    public TransactionServiceImpl(TransactionRepository repo,
                                  AccountClient accountClient,
                                  NotificationClient notificationClient) {
        this.repo = repo;
        this.accountClient = accountClient;
        this.notificationClient = notificationClient;
    }

    @Override
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallback")
    public Transaction deposit(String accountNumber, double amount) {

        accountClient.updateBalance(accountNumber, amount);

        Transaction t = new Transaction();
        t.setTransactionId(UUID.randomUUID().toString());
        t.setType("DEPOSIT");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(accountNumber);

        repo.save(t);
        notificationClient.sendNotification("Deposit successful");

        return t;
    }

    @Override
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallback")
    public Transaction withdraw(String accountNumber, double amount) {

        accountClient.updateBalance(accountNumber, -amount);

        Transaction t = new Transaction();
        t.setTransactionId(UUID.randomUUID().toString());
        t.setType("WITHDRAW");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(accountNumber);

        repo.save(t);
        notificationClient.sendNotification("Withdrawal successful");

        return t;
    }

    @Override
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallback")
    public Transaction transfer(String from, String to, double amount) {

        accountClient.updateBalance(from, -amount);
        accountClient.updateBalance(to, amount);

        Transaction t = new Transaction();
        t.setTransactionId(UUID.randomUUID().toString());
        t.setType("TRANSFER");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(from);
        t.setDestinationAccount(to);

        repo.save(t);
        notificationClient.sendNotification("Transfer successful");

        return t;
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) {
        return repo.findBySourceAccount(accountNumber);
    }

    public Transaction fallback(String accountNumber, double amount, Exception e) {
        logger.error("Fallback triggered due to {}", e.getMessage());

        Transaction t = new Transaction();
        t.setTransactionId(UUID.randomUUID().toString());
        t.setStatus("FAILED");
        t.setTimestamp(new Date());
        t.setType("ERROR");

        return t;
    }
}
