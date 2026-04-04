package com.mental.health.care.backend.service;
import com.mental.health.care.backend.model.Psikiater;
import com.mental.health.care.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.mental.health.care.backend.repository.ChatSessionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final Map<String, Boolean> paymentDatabase = new HashMap<>();
    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public String getPrice(String noStr) {
        return userRepository.findByNoStr(noStr)
            .map(p -> {
                if (p.getHargaKonsultasi() != null) {
                    return "Rp" + String.format("%,d", p.getHargaKonsultasi()).replace(',', '.');
                }
                return "-";
            })
            .orElse("-");
    }

    public String generateQrUrl(String noStr) {
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=PAYMENT_STR_" + noStr;
    }
    
    public boolean checkIsPaid(String noStr) {
        return paymentDatabase.getOrDefault(noStr, false);
    }

    public void processPayment(String patientId, String noStr) {
        paymentDatabase.put(noStr, true);
        //Cek sesi
        chatSessionRepository.findByPatientIdAndDoctorIdAndStatus(patientId, noStr, "ACTIVE")
            .orElseGet(() -> {
                com.mental.health.care.backend.model.ChatSession session = com.mental.health.care.backend.model.ChatSession.builder()
                        .patientId(patientId)
                        .doctorId(noStr)
                        .status("ACTIVE")
                        .createdAt(new java.util.Date())
                        .updatedAt(new java.util.Date())
                        .build();
                com.mental.health.care.backend.model.ChatSession savedSession = chatSessionRepository.save(session);
                messagingTemplate.convertAndSend("/topic/doctor/" + noStr, "NEW_SESSION");
                return savedSession;
            });
    }

    public List<Psikiater> getAllPsikiaters() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user instanceof Psikiater)
                .map(user -> (Psikiater) user)
                .collect(Collectors.toList());
    }

    public String getNamaPsikiater(String noStr) {
        return userRepository.findByNoStr(noStr)
                .filter(user -> user instanceof Psikiater)
                .map(user -> (Psikiater) user)
                .map(Psikiater::getNamaLengkap)
                .orElse("-");
    }
}