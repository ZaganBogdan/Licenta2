package com.example.chat_service.repositories;

import com.example.chat_service.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Message, Long> {
}
