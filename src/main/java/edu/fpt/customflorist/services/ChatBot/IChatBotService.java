package edu.fpt.customflorist.services.ChatBot;

import edu.fpt.customflorist.dtos.ChatBot.ChatMessageDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatRequestDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatResponseDTO;

import java.util.List;

public interface IChatBotService {
    ChatResponseDTO processMessage(ChatRequestDTO request);
    List<ChatResponseDTO> getChatHistory();
}
