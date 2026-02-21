package com.mental.health.care.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerResponseDTO {
    private int id;
    private String text;
    private String image;
    private String dimension;
}
