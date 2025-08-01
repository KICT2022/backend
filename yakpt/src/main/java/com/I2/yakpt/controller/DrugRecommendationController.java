package com.I2.yakpt.controller;


import com.I2.yakpt.dto.ChatCompletionRequest;
import com.I2.yakpt.dto.Message;
import com.I2.yakpt.service.DrugRecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DrugRecommendationController {

    private final DrugRecommendationService drugRecommendationService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.system-prompt.drug-recommend}")
    private String drugRecommendPrompt;

    @Value("${openai.temperature}")
    private double temperature;

    @Value("${openai.max-tokens}")
    private int maxTokens;

    @Autowired
    public DrugRecommendationController(DrugRecommendationService drugRecommendationService) {
        this.drugRecommendationService = drugRecommendationService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        log.info("Received chat request with prompt: {}", prompt);

        Message systemMessage = new Message("system", drugRecommendPrompt);
        Message userMessage = new Message("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(java.util.List.of(systemMessage, userMessage))
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();

        return drugRecommendationService.getChatCompletion(request);
    }
}
