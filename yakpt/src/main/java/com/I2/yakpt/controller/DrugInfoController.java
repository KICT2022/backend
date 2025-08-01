package com.I2.yakpt.controller;


import com.I2.yakpt.dto.ChatCompletionRequest;
import com.I2.yakpt.dto.Message;
import com.I2.yakpt.service.DrugInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class DrugInfoController {



    private final DrugInfoService drugInfoService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.system-prompt.drug-info}")
    private String drugInfoPrompt;

    @Value("${openai.temperature}")
    private double temperature;

    @Value("${openai.max-tokens}")
    private int maxTokens;

    @Autowired
    public DrugInfoController(DrugInfoService drugInfoService) {
        this.drugInfoService = drugInfoService;
    }

    @PostMapping("/chat/drug-info")
    public String getDrugInfo(@RequestBody String drugName) {
        log.info("Received drug info request for: {}", drugName);

        if (drugName == null || drugName.trim().isEmpty()) {
            return "약 이름을 입력해주세요.";
        }

        String userPrompt = drugName.trim() + "에 대해 간단하게 설명해줘. 복용법, 부작용, 구매처 위주로 알려줘.";

        Message systemMessage = new Message("system", drugInfoPrompt);
        Message userMessage = new Message("user", userPrompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(systemMessage, userMessage))
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();

        return drugInfoService.getChatCompletion(request);
    }
}