package com.mental.health.care.backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "questions")
public class Question {
   @Id
    private String id;
    private int questionId;
    private String question;
    private List<Answer> answers;
}
