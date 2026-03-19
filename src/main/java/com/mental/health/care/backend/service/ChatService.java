package com.mental.health.care.backend.service;

import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class ChatService {

    private final GoogleGenAiChatModel chatModel;

    @Value("classpath:ai-rules.txt")
    private Resource systemRulesResource;

    public ChatService(GoogleGenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askGemini(String userMessage) {
        try {
            String systemInstruction = systemRulesResource.getContentAsString(StandardCharsets.UTF_8);
            
            return chatModel.call(systemInstruction + userMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return "Maaf, asisten AI sedang tidak stabil: " + e.getMessage();
        }
    }
}