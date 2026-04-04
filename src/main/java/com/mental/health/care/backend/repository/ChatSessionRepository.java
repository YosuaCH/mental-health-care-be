package com.mental.health.care.backend.repository;

import com.mental.health.care.backend.model.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {
    Optional<ChatSession> findByPatientIdAndDoctorIdAndStatus(String patientId, String doctorId, String status);
    List<ChatSession> findByDoctorIdAndStatus(String doctorId, String status);
    List<ChatSession> findByPatientIdAndStatus(String patientId, String status);
}
