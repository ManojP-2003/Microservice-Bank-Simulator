package com.service;

import com.model.Account;
import com.repository.AccountRepository;
import com.exceptions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Test
    void testCreateAccount() {
        AccountRepository repo = mock(AccountRepository.class);
        AccountServiceImpl service = new AccountServiceImpl(repo);

        Account acc = new Account();
        acc.setHolderName("Manoj");
        acc.setBalance(100.0);

        when(repo.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account created = service.createAccount(acc);

        assertNotNull(created.getAccountNumber()); // changed: we generate accountNumber
        verify(repo, times(1)).save(any(Account.class));
    }

    @Test
    void updateBalance_InsufficientBalance_ThrowsException() {
        AccountRepository repo = mock(AccountRepository.class);
        AccountServiceImpl service = new AccountServiceImpl(repo);

        Account account = new Account("1", "ACC123", "John Doe", 100.0, "ACTIVE", new Date());
        when(repo.findByAccountNumber("ACC123")).thenReturn(account);

        assertThrows(InsufficientBalanceException.class, () -> {
            service.updateBalance("ACC123", -200.0);
        });

        verify(repo, never()).save(any(Account.class));
    }

    @Test
    void getAccountByNumber_AccountNotFound_ThrowsException() {
        AccountRepository repo = mock(AccountRepository.class);
        AccountServiceImpl service = new AccountServiceImpl(repo);

        when(repo.findByAccountNumber("NONEXISTENT")).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            service.getAccountByNumber("NONEXISTENT");
        });
    }

    @Test
    void updateBalance_DepositSuccess_UpdatesBalance() {
        AccountRepository repo = mock(AccountRepository.class);
        AccountServiceImpl service = new AccountServiceImpl(repo);

        Account account = new Account("1", "ACC123", "John Doe", 1000.0, "ACTIVE", new Date());
        when(repo.findByAccountNumber("ACC123")).thenReturn(account);
        when(repo.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = service.updateBalance("ACC123", 500.0);

        assertEquals(1500.0, result.getBalance()); // 1000 + 500 = 1500
        verify(repo, times(1)).save(account);
    }
}
