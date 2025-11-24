package com.controllers;

import com.model.Account;
import com.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        logger.info("API CREATE ACCOUNT");
        return service.createAccount(account);
    }

    @GetMapping("/{accountNumber}")
    public Account get(@PathVariable String accountNumber) {
        logger.info("API GET ACCOUNT = {}", accountNumber);
        return service.getAccountByNumber(accountNumber);
    }

    @PutMapping("/{accountNumber}/balance")
    public Account updateBalance(@PathVariable String accountNumber,
                                 @RequestParam double amount) {
        logger.info("API UPDATE BALANCE");
        return service.updateBalance(accountNumber, amount);
    }

    @PutMapping("/{accountNumber}/status")
    public Account updateStatus(@PathVariable String accountNumber,
                                @RequestParam String status) {
        logger.info("API UPDATE STATUS");
        return service.updateStatus(accountNumber, status);
    }

    @DeleteMapping("/{accountNumber}")
    public String delete(@PathVariable String accountNumber) {
        boolean deleted = service.deleteAccount(accountNumber);
        return deleted ? "Account deleted" : "Account not found";
    }
}
