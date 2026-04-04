package com.mental.health.care.backend.mapper;

import com.mental.health.care.backend.dto.ChatMessageResponseDTO;
import com.mental.health.care.backend.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ChatMessageMapper {

    public ChatMessage toEntity(ChatMessageResponseDTO dto) {
        return ChatMessage.builder()
                .roomId(dto.getRoomId())
                .senderName(dto.getSenderName())
                .content(dto.getContent())
                .isRead(dto.isRead())
                .timestamp(new Date()) 
                .build();
    }

    public ChatMessageResponseDTO toDto(ChatMessage entity) {
        return ChatMessageResponseDTO.builder()
                .roomId(entity.getRoomId())
                .senderName(entity.getSenderName())
                .content(entity.getContent())
                .isRead(entity.isRead())
                .timestamp(entity.getTimestamp().toString())
                .build();
    }
}