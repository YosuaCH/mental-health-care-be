package com.mental.health.care.backend.service;

import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final GoogleGenAiChatModel chatModel;

    public ChatService(GoogleGenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askGemini(String userMessage) {
        try {
            String systemInstruction = "Kamu adalah asisten kesehatan mental yang empati. ";
            return chatModel.call(systemInstruction + userMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error Detail: " + e.getMessage();
        }
    }
}