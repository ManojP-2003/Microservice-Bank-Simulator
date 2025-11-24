package com.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void updateBalance(String accountNumber, double amount) {
        String url = "http://localhost:8081/api/accounts/" + accountNumber + "/balance?amount=" + amount;
        restTemplate.put(url, null);
    }
}
