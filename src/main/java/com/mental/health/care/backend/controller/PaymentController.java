package com.mental.health.care.backend.controller;

import com.mental.health.care.backend.service.PaymentService;
import com.mental.health.care.backend.service.EmailService;
import com.mental.health.care.backend.repository.UserRepository;
import com.mental.health.care.backend.model.BaseUser;
import com.mental.health.care.backend.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @GetMapping("/all-doctors")
    public ResponseEntity<?> getAllDoctors() {
        return ResponseEntity.ok(paymentService.getAllPsikiaters());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getPaymentInfo(@RequestParam String noStr) {
        Map<String, Object> response = new HashMap<>();
        response.put("namaPsikiater", paymentService.getNamaPsikiater(noStr));
        response.put("noStr", noStr);
        response.put("price", paymentService.getPrice(noStr));
        response.put("qrUrl", paymentService.generateQrUrl(noStr));
        response.put("isPaid", paymentService.checkIsPaid(noStr));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulate-success")
    public ResponseEntity<Map<String, String>> simulateSuccess(@RequestParam String patientId, @RequestParam String noStr) {
        paymentService.processPayment(patientId, noStr);
        
        // Logika Pengiriman Email
        try {
            BaseUser user = userRepository.findById(patientId).orElse(null);
            if (user != null) {
                String transactionId = "MHC-" + (10000 + new Random().nextInt(90000));
                String doctorName = paymentService.getNamaPsikiater(noStr);
                String price = paymentService.getPrice(noStr);
                
                String displayName = (user instanceof Client c) ? c.getUsername() : user.getEmail();
                
                emailService.sendPaymentSuccessEmail(user.getEmail(), displayName, doctorName, price, transactionId);
            }
        } catch (MessagingException e) {
            System.err.println("Gagal mengirim email pembayaran: " + e.getMessage());
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pembayaran berhasil diverifikasi oleh server untuk STR: " + noStr);
        response.put("status", "PAID");
        
        return ResponseEntity.ok(response);
    }
}