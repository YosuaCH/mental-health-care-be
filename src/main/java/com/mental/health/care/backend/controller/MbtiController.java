package com.mental.health.care.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.dto.MbtiTypeResponseDTO;
import com.mental.health.care.backend.dto.WebResponseDTO;
import com.mental.health.care.backend.service.MbtiTypeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/mbti")
@RequiredArgsConstructor
public class MbtiController {
    private final MbtiTypeService mbtiTypeService;

    @GetMapping("/{code}")
    public ResponseEntity<WebResponseDTO> getMbtiDetail(@PathVariable String code) {
        MbtiTypeResponseDTO mbtiData = mbtiTypeService.getMbtiDetail(code);
        
        WebResponseDTO response = WebResponseDTO.builder()
                .message("Berhasil mengambil data MBTI: " + code)
                .data(mbtiData)
                .build();
                
        return ResponseEntity.ok(response);
    }
}
