package com.I2.yakpt.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    private String model;
    private List<com.I2.yakpt.dto.Message> messages;
    private double temperature;

    @JsonProperty("max_tokens")
    private int maxTokens;
}