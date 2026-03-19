package com.mental.health.care.backend.controller;

import com.mental.health.care.backend.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
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