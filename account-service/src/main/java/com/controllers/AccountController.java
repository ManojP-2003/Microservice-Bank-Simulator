package com.controllers;

import com.model.Account;
import com.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        logger.info("API CREATE ACCOUNT for: {}", account.getHolderName());
        Account created = service.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> get(@PathVariable String accountNumber) {
        logger.info("API GET ACCOUNT = {}", accountNumber);
        Account account = service.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }


    @PutMapping("/{accountNumber}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable String accountNumber,
                                                 @RequestParam double amount) {
        logger.info("API UPDATE BALANCE for: {} by amount: {}", accountNumber, amount);
        Account updated = service.updateBalance(accountNumber, amount);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<Account> updateStatus(@PathVariable String accountNumber,
                                                @RequestParam String status) {
        logger.info("API UPDATE STATUS for: {} to {}", accountNumber, status);
        Account updated = service.updateStatus(accountNumber, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> delete(@PathVariable String accountNumber) {
        logger.info("API DELETE ACCOUNT: {}", accountNumber);
        service.deleteAccount(accountNumber);
        return ResponseEntity.ok("Account deleted successfully");
    }
}
