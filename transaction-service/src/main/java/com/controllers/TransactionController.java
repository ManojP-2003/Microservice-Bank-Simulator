package com.controllers;

import com.model.Transaction;
import com.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam String accountNumber,
                               @RequestParam double amount) {
        logger.info("API: Deposit");
        return service.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam String accountNumber,
                                @RequestParam double amount) {
        logger.info("API: Withdraw");
        return service.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam String from,
                                @RequestParam String to,
                                @RequestParam double amount) {
        logger.info("API: Transfer");
        return service.transfer(from, to, amount);
    }

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> get(@PathVariable String accountNumber) {
        logger.info("API: Transactions");
        return service.getTransactions(accountNumber);
    }
}
