package com.mental.health.care.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payment_transactions")
public class PaymentTransaction {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private Long amount;
    private String status;
    private Date timestamp;
    private String transactionId;
}
