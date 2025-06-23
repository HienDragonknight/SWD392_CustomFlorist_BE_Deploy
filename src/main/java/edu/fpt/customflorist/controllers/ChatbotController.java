package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.ChatBot.ChatMessageDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatRequestDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatResponseDTO;
import edu.fpt.customflorist.services.ChatBot.IChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/api/v1/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class ChatbotController {
    private final IChatBotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponseDTO> processMessage(@RequestBody ChatRequestDTO request) {
        return ResponseEntity.ok(chatbotService.processMessage(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatResponseDTO>> getChatHistory() {
        return ResponseEntity.ok(chatbotService.getChatHistory());
    }
}
