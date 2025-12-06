package com.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    @PostMapping("/notifications/send")
    String sendNotification(@RequestBody Map<String, String> request);
}
