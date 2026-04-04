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

    //broadcast message
    @MessageMapping("/chat.send")
    public void processMessage(@Payload ChatMessageResponseDTO chatMessage) {
        ChatMessageResponseDTO savedMessage = chatMessageService.saveMessage(chatMessage);
        String roomId = chatMessage.getRoomId();
        messagingTemplate.convertAndSend("/topic/room/" + roomId, savedMessage);
        
        // Broadcast ke global buat nambahin badge
        String[] parts = roomId.split("_");
        if(parts.length >= 3) {
            messagingTemplate.convertAndSend("/topic/global/" + parts[1], savedMessage);
            messagingTemplate.convertAndSend("/topic/global/" + parts[2], savedMessage);
        }
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

    @PutMapping("/api/v1/chat/history/{roomId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String roomId, @RequestParam String readerName) {
        chatMessageService.markRoomAsRead(roomId, readerName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/chat/unread/{roomId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable String roomId, @RequestParam String viewerName) {
        return ResponseEntity.ok(chatMessageService.getUnreadCount(roomId, viewerName));
    }
}