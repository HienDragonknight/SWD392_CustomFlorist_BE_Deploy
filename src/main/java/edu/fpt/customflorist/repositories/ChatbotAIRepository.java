package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.ChatbotAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatbotAIRepository extends JpaRepository<ChatbotAI, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM chatbot_ai WHERE user_id = ?1 ORDER BY timestamp DESC")
    List<ChatbotAI> findByUserId (Long userId);
}
