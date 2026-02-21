package com.mental.health.care.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MbtiTypeResponseDTO {
    private String code;
    private String title;
    private String desc;
    private String longDesc;
    private List<String> traits;
    private List<String> pros;
    private List<String> cons;
    private List<String> developmentTips;
}
