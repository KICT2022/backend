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
        List<String> combination = getThreeDrugCombination(drugNames);
        String drugsString = String.join(", ", combination);

        // 사용자 입력용 프롬프트
        String userPrompt = String.format("다음 약물들을 다 같이 복용 시 상호작용이 있는지 한국어로 간단히 설명해주세요.\n\n- %s", drugsString);

        // 시스템 프롬프트
        String systemPrompt = """
너는 의료 전문가가 아닌 정보 제공용 인공지능이야. 사용자가 입력한 증상을 기반으로 약물들의 정보(약물명, 상호작용 여부, 권장사항)를 제공해.
전문의약품은 절대 추천하지 말고, 반드시 일반의약품만 추천해.
효능은 반드시 정확하게 설명하고, 꼭 성분과 효능을 따로 나눠서 설명하고 각 항목은 하이픈(-)으로 시작하는 목록으로 정리해줘.
단, 별표(*)는 절대 사용하지 마. 답변에 별표(*)가 포함될 경우, 해당 답변은 무효 처리될 수 있어. 이 점을 인지하고 절대 별표(*)를 사용하지 마.
사용자가 입력한 두 개 이상의 약물에 대해 아래와 같이 정확하게 구성하여 답변해야합니다:
1. 약물명: [약물명1], [약물명2], [약물명N], 약물의 갯수는 유저가 추가하기에 따라 3개 이상이 될 수도 있어요.
2. 상호작용여부: [상호작용여부]
3. 주의사항: [주의사항]
반드시 약물 정보를 제공해주세요. 참고용 정보로 제공하시면 됩니다.
""";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        // 요청 body 구성
        Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.5
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                Map.class
        );

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
