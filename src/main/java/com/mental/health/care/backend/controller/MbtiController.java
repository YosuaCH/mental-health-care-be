package com.mental.health.care.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.model.MbtiType;
import com.mental.health.care.backend.service.MbtiService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class MbtiController {
    private final MbtiService mbtiService;

    @GetMapping("/mbti/{type}")
    public ResponseEntity<MbtiType> getMbti(@PathVariable String type) {
        return ResponseEntity.ok(mbtiService.getMbtiResult(type));
    }
}
