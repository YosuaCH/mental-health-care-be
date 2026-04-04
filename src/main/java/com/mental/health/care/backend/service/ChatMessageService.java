package com.mental.health.care.backend.service;

import com.mental.health.care.backend.dto.ChatMessageResponseDTO;
import com.mental.health.care.backend.mapper.ChatMessageMapper;
import com.mental.health.care.backend.model.ChatMessage;
import com.mental.health.care.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.mapper.UserMapper;
import com.mental.health.care.backend.repository.ChatSessionRepository;
import com.mental.health.care.backend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatMessageMapper mapper;
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // Menyimpan chat baru ke MongoDB
    public ChatMessageResponseDTO saveMessage(ChatMessageResponseDTO chatMessageDTO) {
        ChatMessage message = mapper.toEntity(chatMessageDTO);
        ChatMessage savedMessage = repository.save(message);
        return mapper.toDto(savedMessage);
    }

    public List<ChatMessageResponseDTO> getChatHistory(String roomId) {
        return repository.findByRoomIdOrderByTimestampAsc(roomId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getActivePatients(String doctorId) {
        List<String> activePatientIds = chatSessionRepository.findByDoctorIdAndStatus(doctorId, "ACTIVE")
                .stream()
                .map(session -> session.getPatientId())
                .collect(Collectors.toList());
        
        return userRepository.findAllById(activePatientIds).stream()
                .map(user -> userMapper.toResponseDTO(user))
                .collect(Collectors.toList());
    }

    public void endSession(String roomId) {
        // format roomId: room_{patientId}_{doctorId}
        String[] parts = roomId.split("_");
        if(parts.length >= 3) {
            String patientId = parts[1];
            String doctorId = parts[2];
            chatSessionRepository.findByPatientIdAndDoctorIdAndStatus(patientId, doctorId, "ACTIVE")
                .ifPresent(session -> {
                    session.setStatus("ENDED");
                    chatSessionRepository.save(session);
                    messagingTemplate.convertAndSend("/topic/doctor/" + doctorId, "NEW_SESSION");
                });
        }
        repository.deleteByRoomId(roomId);
    }

    public void markRoomAsRead(String roomId, String readerName) {
        List<ChatMessage> unreadMessages = repository.findByRoomIdAndIsRead(roomId, false);
        boolean changed = false;
        for (ChatMessage msg : unreadMessages) {
            if (!msg.getSenderName().equals(readerName)) {
                msg.setRead(true);
                changed = true;
            }
        }
        if (changed) {
            repository.saveAll(unreadMessages);
        }
    }

    public long getUnreadCount(String roomId, String viewerName) {
        return repository.countByRoomIdAndSenderNameNotAndIsRead(roomId, viewerName, false);
    }
}