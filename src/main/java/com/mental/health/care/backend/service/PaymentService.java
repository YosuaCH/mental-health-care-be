package com.mental.health.care.backend.service;
import com.mental.health.care.backend.model.Psikiater;
import com.mental.health.care.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final Map<String, Boolean> paymentDatabase = new HashMap<>();
    private final UserRepository userRepository;

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

    public void processPayment(String noStr) {
        paymentDatabase.put(noStr, true);
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