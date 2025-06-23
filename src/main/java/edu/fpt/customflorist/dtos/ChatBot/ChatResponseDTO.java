package edu.fpt.customflorist.dtos.ChatBot;

import edu.fpt.customflorist.models.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatResponseDTO {
    private Long chatSessionId;

    private User user;

    private String query;

    private String response;

    private LocalDateTime timestamp;

    private Boolean isActive;
}
