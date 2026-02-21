package com.mental.health.care.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mental.health.care.backend.model.MbtiType;

public interface MbtiRepository extends MongoRepository<MbtiType, String> {
    
}
