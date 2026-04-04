package com.mental.health.care.backend.controller;

import com.mental.health.care.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
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
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pembayaran berhasil diverifikasi oleh server untuk STR: " + noStr);
        response.put("status", "PAID");
        
        return ResponseEntity.ok(response);
    }
}