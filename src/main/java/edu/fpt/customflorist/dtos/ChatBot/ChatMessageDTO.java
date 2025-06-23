package edu.fpt.customflorist.dtos.ChatBot;

import edu.fpt.customflorist.models.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {

    private Long chatSessionId;

    private User user;

    private String query;

    private String response;

    private LocalDateTime timestamp;

    private Boolean isActive;
}
