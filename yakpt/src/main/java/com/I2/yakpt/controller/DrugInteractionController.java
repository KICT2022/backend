package com.I2.yakpt.controller;

import com.I2.yakpt.dto.InteractionRequest;
import com.I2.yakpt.service.DrugInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugInteractionController {

    private final DrugInteractionService drugInteractionService;

    @PostMapping("/check")
    public String checkDrugInteractions(@RequestBody InteractionRequest request) {
        String rawMessage = request.getMessage();

        if (rawMessage == null || rawMessage.isBlank()) {
            return "약물 이름을 입력해주세요.";
        }

        List<String> drugList = Arrays.stream(rawMessage.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (drugList.size() < 2) {
            return "두 개 이상의 약물을 쉼표로 구분해 주세요.";
        }

        return drugInteractionService.askDrugInteraction(drugList);
    }
}
