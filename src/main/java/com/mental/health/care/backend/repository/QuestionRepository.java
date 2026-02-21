package com.mental.health.care.backend.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.mental.health.care.backend.model.Question;

public interface QuestionRepository extends MongoRepository<Question, String> {

}
