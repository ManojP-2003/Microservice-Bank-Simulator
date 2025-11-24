package com.controllers;

import com.model.Transaction;
import com.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam String accountNumber,
                               @RequestParam double amount) {
        return service.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam String accountNumber,
                                @RequestParam double amount) {
        return service.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam String from,
                                @RequestParam String to,
                                @RequestParam double amount) {
        return service.transfer(from, to, amount);
    }

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> get(@PathVariable String accountNumber) {
        return service.getTransactions(accountNumber);
    }
}
