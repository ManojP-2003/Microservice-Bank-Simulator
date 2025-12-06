package com.service;

import com.client.AccountServiceClient;
import com.client.NotificationServiceClient;
import com.model.Transaction;
import com.repository.TransactionRepository;
import com.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository repo;
    private final AccountServiceClient accountServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    public TransactionServiceImpl(TransactionRepository repo,
                                  AccountServiceClient accountServiceClient,
                                  NotificationServiceClient notificationServiceClient) {
        this.repo = repo;
        this.accountServiceClient = accountServiceClient;
        this.notificationServiceClient = notificationServiceClient;
    }

    private String generateTxId() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int random = (int) (Math.random() * 900) + 100;
        return "TXN-" + date + "-" + random;
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private void notify(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        try {
            notificationServiceClient.sendNotification(body);
        } catch (Exception e) {
            logger.warn("Notification failed: {}", e.getMessage());
        }
    }

    @Override
    public Transaction deposit(String accountNumber, double amount) {

        validateAmount(amount);

        try {
            accountServiceClient.updateBalance(accountNumber, amount);
        } catch (Exception ex) {
            throw ex; // let FeignErrorDecoder convert this to proper exception
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTxId());
        t.setType("DEPOSIT");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(accountNumber);

        repo.save(t);
        notify("Deposit of " + amount + " to " + accountNumber + " successful");

        return t;
    }


    @Override
    public Transaction withdraw(String accountNumber, double amount) {

        validateAmount(amount);

        try {
            accountServiceClient.updateBalance(accountNumber, -amount);
        } catch (Exception ex) {
            throw ex; // FEIGN WILL HANDLE INVALID, INACTIVE, INSUFFICIENT
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTxId());
        t.setType("WITHDRAW");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(accountNumber);

        repo.save(t);
        notify("Withdrawal of " + amount + " from " + accountNumber + " successful");

        return t;
    }


    @Override
    public Transaction transfer(String from, String to, double amount) {

        validateAmount(amount);

        if (from.equals(to)) {
            throw new InvalidAmountException("Cannot transfer to same account");
        }

        try {
            accountServiceClient.updateBalance(from, -amount);
            accountServiceClient.updateBalance(to, amount);
        } catch (Exception ex) {
            throw ex; // FEIGN WILL HANDLE ERRORS
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTxId());
        t.setType("TRANSFER");
        t.setAmount(amount);
        t.setTimestamp(new Date());
        t.setStatus("SUCCESS");
        t.setSourceAccount(from);
        t.setDestinationAccount(to);

        repo.save(t);
        notify("Transferred " + amount + " from " + from + " to " + to);

        return t;
    }

    @Override
    public List<Transaction> getTransactions(String account) {
        List<Transaction> result = new ArrayList<>();
        result.addAll(repo.findBySourceAccount(account));
        result.addAll(repo.findByDestinationAccount(account));
        return result;
    }
}
