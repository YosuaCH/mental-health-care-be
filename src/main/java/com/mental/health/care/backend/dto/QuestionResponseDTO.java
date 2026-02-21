package com.mental.health.care.backend.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionResponseDTO {
    private int questionId;
    private String question;
    private List<AnswerResponseDTO> answers;
}
