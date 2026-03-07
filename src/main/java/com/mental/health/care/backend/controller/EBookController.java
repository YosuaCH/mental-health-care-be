package com.mental.health.care.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.dto.EBookDTO;
import com.mental.health.care.backend.service.EBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ebooks")
@RequiredArgsConstructor
public class EBookController {

    private final EBookService eBookService;

    @GetMapping
    public ResponseEntity<List<EBookDTO>> getMentalHealthBooks() {
        List<EBookDTO> books = eBookService.getMentalHealthBooks("mental health psychology self improvement");
        return ResponseEntity.ok(books);
    }
}
