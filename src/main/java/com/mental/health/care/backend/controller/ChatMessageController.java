package com.mental.health.care.backend.controller;

import com.mental.health.care.backend.dto.ChatMessageResponseDTO;
import com.mental.health.care.backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.send")
    public void processMessage(@Payload ChatMessageResponseDTO chatMessage) {
        ChatMessageResponseDTO savedMessage = chatMessageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), savedMessage);
    }

    @GetMapping("/api/v1/chat/history/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDTO>> getChatHistory(@PathVariable String roomId) {
        return ResponseEntity.ok(chatMessageService.getChatHistory(roomId));
    }

    @GetMapping("/api/v1/chat/patients/{doctorId}")
    public ResponseEntity<?> getActivePatients(@PathVariable String doctorId) {
        return ResponseEntity.ok(chatMessageService.getActivePatients(doctorId));
    }

    @DeleteMapping("/api/v1/chat/session/{roomId}")
    public ResponseEntity<?> endSession(@PathVariable String roomId) {
        chatMessageService.endSession(roomId);
        return ResponseEntity.ok().build();
    }
}