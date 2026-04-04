package com.mental.health.care.backend.controller;

import com.mental.health.care.backend.service.ChatAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class ChatAiController {

    private final ChatAiService chatService;

    public ChatAiController(ChatAiService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {
        try {
            return chatService.askGemini(message);
        } catch (Exception e) {
            return "Maaf, terjadi kesalahan saat menghubungi AI: " + e.getMessage();
        }
    }
}