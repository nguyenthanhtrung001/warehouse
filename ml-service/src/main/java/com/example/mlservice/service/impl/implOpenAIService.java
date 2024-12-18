package com.example.mlservice.service.impl;

import com.example.mlservice.service.IOpenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Service
public class implOpenAIService implements IOpenAIService {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public implOpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/").build();
    }

    @Override
    public String getChatResponse(String prompt) {
        int retryCount = 0;
        int maxRetries = 5;
        int backoffTime = 2000; // 2 giây

        while (retryCount < maxRetries) {
            try {
                return webClient.post()
                        .uri("completions")
                        .header("Authorization", "Bearer " + apiKey)
                        .bodyValue(buildRequestBody(prompt))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().value() == 429) {
                    retryCount++;
                    try {
                        System.out.println("429 Too Many Requests - Đợi " + backoffTime + "ms trước khi thử lại lần " + retryCount);
                        Thread.sleep(backoffTime); // Đợi trước khi thử lại
                        backoffTime *= 2; // Tăng gấp đôi thời gian chờ mỗi lần thử lại
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Quá trình thử lại bị gián đoạn", ie);
                    }
                } else {
                    throw e; // Không thử lại nếu lỗi không phải 429
                }
            }
        }
        throw new RuntimeException("Không thể lấy phản hồi sau khi thử lại nhiều lần");
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo"); // Thay thế model cũ bằng "gpt-3.5-turbo"
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);
        return requestBody;
    }
}
