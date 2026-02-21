package com.mental.health.care.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mental.health.care.backend.dto.QuestionResponseDTO;
import com.mental.health.care.backend.mapper.QuestionMapper;
import com.mental.health.care.backend.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public List<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }
}