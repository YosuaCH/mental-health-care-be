package com.mental.health.care.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Answer {
    private int id;
    private String text;
    private String image;
    private String dimension;
}
