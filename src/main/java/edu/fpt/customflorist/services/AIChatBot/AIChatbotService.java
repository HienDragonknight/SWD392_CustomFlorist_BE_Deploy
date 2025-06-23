package edu.fpt.customflorist.services.AIChatBot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AIChatbotService {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateAIResponse(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Xây dựng payload JSON theo định dạng yêu cầu của Google
            ObjectNode requestBody = objectMapper.createObjectNode();
            ArrayNode contentsArray = objectMapper.createArrayNode();
            ObjectNode contentObject = objectMapper.createObjectNode();
            ArrayNode partsArray = objectMapper.createArrayNode();
            ObjectNode partObject = objectMapper.createObjectNode();

            // Chỉ gửi tin nhắn mới nhất
            partObject.put("text", message);
            partsArray.add(partObject);
            contentObject.set("parts", partsArray);
            contentsArray.add(contentObject);
            requestBody.set("contents", contentsArray);

            // Tạo request entity
            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

            // Gắn API key vào URL dưới dạng query parameter
            String urlWithKey = apiUrl + "?key=" + apiKey;

            // Gọi API Google Generative Language
            String response = restTemplate.postForObject(urlWithKey, request, String.class);

            // Xử lý response
            JsonNode responseJson = objectMapper.readTree(response);
            JsonNode candidates = responseJson.path("candidates");

            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).path("text").asText();
                }
            }

            return "Xin lỗi, tôi không thể xử lý tin nhắn của bạn lúc này.";
        } catch (Exception e) {
            log.error("Error calling Google Generative Language API", e);
            return "Xin lỗi, tôi không thể xử lý tin nhắn của bạn lúc này. Vui lòng thử lại sau.";
        }
    }
}