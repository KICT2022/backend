package com.I2.yakpt.service;

import com.I2.yakpt.dto.ChatCompletionRequest;
import com.I2.yakpt.dto.ChatCompletionResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DrugInfoService {

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.api-key}")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
        log.info("DrugInfoService initialized. URL: {}", apiUrl);
    }

    public String getChatCompletion(ChatCompletionRequest request) {
        if (request == null || request.getMessages() == null || request.getMessages().isEmpty()) {
            log.error("ChatCompletionRequest 객체 또는 messages 리스트가 null이거나 비어있습니다.");
            return "요청 데이터가 올바르지 않습니다. 다시 시도해 주세요.";
        }

        log.info("API Request JSON Payload: {}", request.toString());

        try {
            Mono<ChatCompletionResponse> responseMono = webClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class);

            ChatCompletionResponse response = responseMono.block();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            } else {
                log.warn("API 응답에 'choices'가 비어 있거나 올바르지 않습니다.");
                return "API 응답 형식이 올바르지 않습니다. 다시 시도해 주세요.";
            }
        } catch (WebClientResponseException e) {
            log.error("OpenAI API 호출 중 오류 발생: HTTP {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "API 호출 중 오류가 발생했습니다: " + e.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return "예상치 못한 오류가 발생했습니다: " + e.getMessage();
        }
    }
}