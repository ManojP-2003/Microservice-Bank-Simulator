package com.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendNotification(String msg) {
        restTemplate.postForObject(
                "http://localhost:8083/api/notifications/send",
                msg,
                String.class
        );
    }
}
