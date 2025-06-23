package edu.fpt.customflorist.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private ChatSession session;
    @Lob
    private String content;
    private boolean isFromUser;
    private LocalDateTime timestamp = LocalDateTime.now();

}
