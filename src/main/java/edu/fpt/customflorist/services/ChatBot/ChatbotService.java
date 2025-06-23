package edu.fpt.customflorist.services.ChatBot;

import edu.fpt.customflorist.components.JwtTokenUtils;
import edu.fpt.customflorist.dtos.ChatBot.ChatMessageDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatRequestDTO;
import edu.fpt.customflorist.dtos.ChatBot.ChatResponseDTO;
import edu.fpt.customflorist.models.ChatMessage;
import edu.fpt.customflorist.models.ChatSession;
import edu.fpt.customflorist.models.ChatbotAI;
import edu.fpt.customflorist.repositories.ChatMessageRepository;
import edu.fpt.customflorist.repositories.ChatSessionRepository;
import edu.fpt.customflorist.repositories.ChatbotAIRepository;
import edu.fpt.customflorist.repositories.UserRepository;
import edu.fpt.customflorist.services.AIChatBot.AIChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotService implements IChatBotService {

    private final AIChatbotService aiChatbotService;
    private final UserRepository userRepository;
    private final ChatbotAIRepository chatbotAIRepository;

    @Override
    @Transactional
    public ChatResponseDTO processMessage(ChatRequestDTO request) {
        var userId = JwtTokenUtils.getUserIdFromToken();
        var user = userRepository.findById(userId).orElseThrow();
        // Lấy hoặc tạo phiên chat mới

//        String systemPrompt = "Bạn là trợ lý ảo của shop hoa FlowerShop. " +
//                "Bạn có thể trả lời các câu hỏi về sản phẩm, khuyến mãi, đặt hàng, giao hàng. " +
//                "Shop mở cửa từ 8:00 sáng đến 20:00 tối, từ thứ Hai đến Chủ Nhật. " +
//                "Hotline liên hệ: 1900-xxxx. " +
//                "Trả lời ngắn gọn và thân thiện. " +
//                "Nếu không biết câu trả lời, hãy đề nghị khách hàng liên hệ với shop qua hotline hoặc email.";

        // Gọi API AI để lấy phản hồi (truyền cả lịch sử chat)
        String botResponse = aiChatbotService.generateAIResponse(request.getMessage());

        ChatbotAI chatbotAI = new ChatbotAI();
        chatbotAI.setQuery(request.getMessage());
        chatbotAI.setResponse(botResponse);
        chatbotAI.setTimestamp(LocalDateTime.now());
        chatbotAI.setIsActive(true);
        chatbotAI.setUser(user);

        chatbotAIRepository.save(chatbotAI);

        ChatResponseDTO responseDTO = convertToDTO(chatbotAI);
        return  responseDTO;
    }

    @Override
    @Transactional
    public List<ChatResponseDTO> getChatHistory() {
        var userId = JwtTokenUtils.getUserIdFromToken();
        var user = userRepository.findByUserId(userId).orElseThrow();
        List<ChatbotAI> chatHistory = chatbotAIRepository.findByUserId(user.getUserId());
        return chatHistory.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

//    public List<ChatMessageDTO> getChatHistory(String sessionId) {
//        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId)
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    private ChatResponseDTO convertToDTO(ChatbotAI message) {
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setChatSessionId(message.getChatSessionId());
        dto.setQuery(message.getQuery());
        dto.setResponse(message.getResponse());
        dto.setTimestamp(message.getTimestamp());
        dto.setIsActive(message.getIsActive());
        dto.setUser(message.getUser());
        return dto;
    }
}