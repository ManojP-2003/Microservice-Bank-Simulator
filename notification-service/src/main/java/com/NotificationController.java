package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public String send(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null) {
            message = "";
        }
        logger.info("Notification request received: {}", message);
        notificationService.sendNotification(message);
        return "Notification Sent";
    }
}
