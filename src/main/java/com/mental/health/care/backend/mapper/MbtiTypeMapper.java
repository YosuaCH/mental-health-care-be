package com.mental.health.care.backend.mapper;

import org.springframework.stereotype.Component;

import com.mental.health.care.backend.dto.MbtiTypeResponseDTO;
import com.mental.health.care.backend.model.MbtiType;

@Component
public class MbtiTypeMapper {
    public MbtiTypeResponseDTO toResponseDTO(MbtiType mbtiType) {
        if (mbtiType == null) {
            return null;
        }
        return MbtiTypeResponseDTO.builder()
                .code(mbtiType.getCode())
                .title(mbtiType.getTitle())
                .desc(mbtiType.getDesc())
                .longDesc(mbtiType.getLongDesc())
                .traits(mbtiType.getTraits())
                .pros(mbtiType.getPros())
                .cons(mbtiType.getCons())
                .developmentTips(mbtiType.getDevelopmentTips())
                .build();
    }
}
