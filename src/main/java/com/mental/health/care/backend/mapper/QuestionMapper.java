package com.mental.health.care.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mental.health.care.backend.dto.AnswerResponseDTO;
import com.mental.health.care.backend.dto.QuestionResponseDTO;
import com.mental.health.care.backend.model.Answer;
import com.mental.health.care.backend.model.Question;

@Component
public class QuestionMapper {
    public QuestionResponseDTO toDTO(Question question) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestion(question.getQuestion());
        
        List<AnswerResponseDTO> answerDTOs = question.getAnswers().stream()
            .map(this::toAnswerDTO)
            .collect(Collectors.toList());
            
        dto.setAnswers(answerDTOs);
        return dto;
    }

    private AnswerResponseDTO toAnswerDTO(Answer answer) {
        AnswerResponseDTO dto = new AnswerResponseDTO();
        dto.setId(answer.getId());
        dto.setText(answer.getText());
        dto.setImage(answer.getImage());
        dto.setDimension(answer.getDimension());
        return dto;
    }
}
