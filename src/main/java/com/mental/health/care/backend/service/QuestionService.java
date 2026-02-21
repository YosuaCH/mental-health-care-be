package com.mental.health.care.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mental.health.care.backend.model.Question;
import com.mental.health.care.backend.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}