package com.service;

import com.client.AccountServiceClient;
import com.client.NotificationServiceClient;
import com.model.Transaction;
import com.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repo;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private TransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositSuccess() {
        String accountNumber = "ACC123";
        double amount = 100.0;

        Transaction savedTx = new Transaction();
        savedTx.setTransactionId("TXN-TEST");
        savedTx.setType("DEPOSIT");
        savedTx.setAmount(amount);
        savedTx.setStatus("SUCCESS");

        doNothing().when(accountServiceClient).updateBalance(accountNumber, amount);
        when(repo.save(any(Transaction.class))).thenReturn(savedTx);
        when(notificationServiceClient.sendNotification(anyMap())).thenReturn("OK");

        Transaction result = service.deposit(accountNumber, amount);

        assertNotNull(result);
        assertEquals("DEPOSIT", result.getType());
        assertEquals(amount, result.getAmount());

        verify(accountServiceClient).updateBalance(accountNumber, amount);
        verify(repo).save(any(Transaction.class));
    }

    @Test
    void testWithdrawSuccess() {
        String accountNumber = "ACC123";
        double amount = 50.0;

        Transaction savedTx = new Transaction();
        savedTx.setTransactionId("TXN-TEST");
        savedTx.setType("WITHDRAW");
        savedTx.setAmount(amount);
        savedTx.setStatus("SUCCESS");

        doNothing().when(accountServiceClient).updateBalance(accountNumber, -amount);
        when(repo.save(any(Transaction.class))).thenReturn(savedTx);
        when(notificationServiceClient.sendNotification(anyMap())).thenReturn("OK");

        Transaction result = service.withdraw(accountNumber, amount);

        assertNotNull(result);
        assertEquals("WITHDRAW", result.getType());

        verify(accountServiceClient).updateBalance(accountNumber, -amount);
        verify(repo).save(any(Transaction.class));
    }

    @Test
    void testTransferSuccess() {
        String from = "ACC123";
        String to = "ACC456";
        double amount = 75.0;

        Transaction savedTx = new Transaction();
        savedTx.setTransactionId("TXN-TEST");
        savedTx.setType("TRANSFER");
        savedTx.setAmount(amount);
        savedTx.setStatus("SUCCESS");

        doNothing().when(accountServiceClient).updateBalance(from, -amount);
        doNothing().when(accountServiceClient).updateBalance(to, amount);
        when(repo.save(any(Transaction.class))).thenReturn(savedTx);
        when(notificationServiceClient.sendNotification(anyMap())).thenReturn("OK");

        Transaction result = service.transfer(from, to, amount);

        assertNotNull(result);
        assertEquals("TRANSFER", result.getType());

        verify(accountServiceClient).updateBalance(from, -amount);
        verify(accountServiceClient).updateBalance(to, amount);
        verify(repo).save(any(Transaction.class));
    }

    @Test
    void testDepositInvalidAmount() {
        assertThrows(com.exceptions.InvalidAmountException.class,
                () -> service.deposit("ACC123", -100));
    }

    @Test
    void testWithdrawInvalidAmount() {
        assertThrows(com.exceptions.InvalidAmountException.class,
                () -> service.withdraw("ACC123", 0));
    }

    @Test
    void testTransferSameAccount() {
        assertThrows(com.exceptions.InvalidAmountException.class,
                () -> service.transfer("ACC123", "ACC123", 100));
    }

    @Test
    void testGetTransactions() {
        String accountNumber = "ACC123";
        List<Transaction> expected = Arrays.asList(new Transaction(), new Transaction());

        when(repo.findBySourceAccount(accountNumber)).thenReturn(expected);
        when(repo.findByDestinationAccount(accountNumber)).thenReturn(Collections.emptyList());

        List<Transaction> actual = service.getTransactions(accountNumber);

        assertEquals(2, actual.size());
        verify(repo).findBySourceAccount(accountNumber);
        verify(repo).findByDestinationAccount(accountNumber);
    }
}
