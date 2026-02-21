package com.mental.health.care.backend.model;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Document(collection = "mbti_types")
public class MbtiType {

    @Id
    private String id;

    private String title;
    private String desc;
    private String longDesc;

    private List<String> traits;
    private List<String> pros;
    private List<String> cons;
    private List<String> developmentTips;
}