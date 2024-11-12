package com.example.chat_service.services;

import com.example.chat_service.entities.Message;
import com.example.chat_service.repositories.ChatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @SneakyThrows
    public ResponseEntity<?> addMessage(String messageString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(messageString, Message.class);
        chatRepository.save(message);
        return ResponseEntity.ok(message);
    }

}
