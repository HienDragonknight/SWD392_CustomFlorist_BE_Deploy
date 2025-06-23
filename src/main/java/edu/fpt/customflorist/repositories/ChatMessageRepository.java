package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.dtos.ChatBot.ChatMessageDTO;
import edu.fpt.customflorist.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String id);
}
