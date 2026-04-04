package com.mental.health.care.backend.service;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ChatAiService {

    private final GoogleGenAiChatModel chatModel;

    @Value("classpath:ai-rules.txt")
    private Resource systemRulesResource;

    public ChatAiService(GoogleGenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askGemini(String userMessage) {
        try {
            String systemInstruction = systemRulesResource.getContentAsString(StandardCharsets.UTF_8);

            SystemMessage systemRole = new SystemMessage(systemInstruction);
            UserMessage userRole = new UserMessage(userMessage);
            Prompt prompt = new Prompt(List.of(systemRole, userRole));

            return chatModel.call(prompt).getResult().getOutput().getText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Maaf, asisten AI sedang tidak stabil: " + e.getMessage();
        }
    }
}