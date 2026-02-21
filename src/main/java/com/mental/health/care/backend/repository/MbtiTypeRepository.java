package com.mental.health.care.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mental.health.care.backend.model.MbtiType;

public interface MbtiTypeRepository extends MongoRepository<MbtiType, String> {
    Optional<MbtiType> findByCode(String code);
}
