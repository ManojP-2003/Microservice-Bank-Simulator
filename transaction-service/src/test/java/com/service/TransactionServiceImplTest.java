package com.service;

import com.client.AccountClient;
import com.client.NotificationClient;
import com.model.Transaction;
import com.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Test
    public void testDeposit() {
        TransactionRepository repo = mock(TransactionRepository.class);
        AccountClient accountClient = mock(AccountClient.class);
        NotificationClient notificationClient = mock(NotificationClient.class);

        TransactionServiceImpl service =
                new TransactionServiceImpl(repo, accountClient, notificationClient);

        Transaction result = service.deposit("A1001", 1000);

        assertNotNull(result);
        assertEquals("DEPOSIT", result.getType());
        assertEquals(1000, result.getAmount());
        verify(accountClient, times(1)).updateBalance("A1001", 1000);
        verify(notificationClient, times(1)).sendNotification("Deposit successful");
    }

    @Test
    public void testWithdraw() {
        TransactionRepository repo = mock(TransactionRepository.class);
        AccountClient accountClient = mock(AccountClient.class);
        NotificationClient notificationClient = mock(NotificationClient.class);

        TransactionServiceImpl service =
                new TransactionServiceImpl(repo, accountClient, notificationClient);

        Transaction result = service.withdraw("A1001", 500);

        assertNotNull(result);
        assertEquals("WITHDRAW", result.getType());
        assertEquals(500, result.getAmount());
        verify(accountClient, times(1)).updateBalance("A1001", -500);
        verify(notificationClient, times(1)).sendNotification("Withdrawal successful");
    }

    @Test
    public void testTransfer() {
        TransactionRepository repo = mock(TransactionRepository.class);
        AccountClient accountClient = mock(AccountClient.class);
        NotificationClient notificationClient = mock(NotificationClient.class);

        TransactionServiceImpl service =
                new TransactionServiceImpl(repo, accountClient, notificationClient);

        Transaction result = service.transfer("A1001", "A2002", 300);

        assertNotNull(result);
        assertEquals("TRANSFER", result.getType());
        assertEquals("A1001", result.getSourceAccount());
        assertEquals("A2002", result.getDestinationAccount());
        verify(accountClient, times(1)).updateBalance("A1001", -300);
        verify(accountClient, times(1)).updateBalance("A2002", 300);
        verify(notificationClient, times(1)).sendNotification("Transfer successful");
    }
}
