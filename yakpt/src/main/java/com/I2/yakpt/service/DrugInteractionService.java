package com.I2.yakpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugInteractionService {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askDrugInteraction(List<String> drugNames) {
        // 세 가지 조합만 생성
        List<String> combination = getThreeDrugCombination(drugNames);

        // 프롬프트 생성
        String drugsString = combination.stream().collect(Collectors.joining(", "));
        String prompt = String.format("다음 약물들을 다 같이 복용 시 상호작용이 있는지 한국어로 간단히 설명해주세요.\n\n- %s", drugsString);

        // OpenAI API 호출을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        // API 요청 본문 생성
        Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                },
                "temperature", 0.5
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // OpenAI API에 요청 보내고 응답 받기
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                Map.class
        );

        // 응답에서 모델의 답변 추출
        Map<String, Object> choice = ((Map<String, Object>) ((List<?>) response.getBody().get("choices")).get(0));
        Map<String, String> message = (Map<String, String>) choice.get("message");

        return message.get("content");
    }

    private List<String> getThreeDrugCombination(List<String> drugs) {
        if (drugs.size() >= 3) {
            return drugs.subList(0, 3).stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return drugs.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }
}