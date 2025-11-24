package com.service;

import com.model.Account;
import com.repository.AccountRepository;
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
        acc.setAccountNumber("123");
        acc.setHolderName("Manoj");

        when(repo.save(Mockito.any(Account.class))).thenReturn(acc);

        Account created = service.createAccount(acc);

        assertEquals("123", created.getAccountNumber());
        verify(repo, times(1)).save(acc);
    }
}
