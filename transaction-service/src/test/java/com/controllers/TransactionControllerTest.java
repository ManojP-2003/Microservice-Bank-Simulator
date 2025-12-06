package com.controllers;

import com.service.TransactionService;
import com.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();
    }

    @Test
    public void deposit_ValidRequest_ReturnsOk() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN-20251107-001");
        transaction.setType("DEPOSIT");
        transaction.setAmount(1000);
        transaction.setStatus("SUCCESS");

        when(transactionService.deposit(anyString(), anyDouble())).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/deposit")
                        .param("accountNumber", "A1001")
                        .param("amount", "1000"))
                .andExpect(status().isOk());
    }
}