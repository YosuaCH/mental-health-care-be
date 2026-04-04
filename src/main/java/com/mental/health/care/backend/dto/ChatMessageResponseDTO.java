package com.mental.health.care.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponseDTO {
    private String roomId;
    private String senderName;
    private String content;
    private boolean isRead;
    private String timestamp;
}