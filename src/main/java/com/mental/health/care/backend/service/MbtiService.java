package com.mental.health.care.backend.service;

import org.springframework.stereotype.Service;

import com.mental.health.care.backend.model.MbtiType;
import com.mental.health.care.backend.repository.MbtiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MbtiService {
    private final MbtiRepository mbtiRepository;

    public MbtiType getMbtiResult(String type) {
    return mbtiRepository.findById(type)
            .orElseThrow(() -> new RuntimeException("MBTI not found"));
}
}
