package com.example.chat_service.services;

import com.example.chat_service.entities.Message;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String receiverName, String messageContent) {
        Message message = new Message();
        message.setReceiverName(receiverName);
        message.setMessage(messageContent);
        messagingTemplate.convertAndSendToUser(receiverName, "/notifications", messageContent);
    }

}
