package com.example.chat_service.controllers;

import com.example.chat_service.dtos.NotificationDto;
import com.example.chat_service.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notificationDto) {
        notificationService.sendNotification(notificationDto.getUsername(), notificationDto.getMessage());
        return ResponseEntity.ok("Notification sent successfully");
    }
    @PostMapping("/sendLeaveNotification")
    public ResponseEntity<?> sendLeaveMatchNotification(@RequestBody NotificationDto notificationDto) {
        notificationService.sendNotification(notificationDto.getUsername(), notificationDto.getMessage());
        return ResponseEntity.ok("Notification sent successfully");
    }
}