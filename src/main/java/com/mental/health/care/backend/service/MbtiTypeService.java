package com.mental.health.care.backend.service;

import org.springframework.stereotype.Service;

import com.mental.health.care.backend.dto.MbtiTypeResponseDTO;
import com.mental.health.care.backend.mapper.MbtiTypeMapper;
import com.mental.health.care.backend.model.MbtiType;
import com.mental.health.care.backend.repository.MbtiTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MbtiTypeService {
    private final MbtiTypeRepository mbtiTypeRepository;
    private final MbtiTypeMapper mbtiTypeMapper;

    public MbtiTypeResponseDTO getMbtiDetail(String code) {
        MbtiType mbtiType = mbtiTypeRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Tipe MBTI " + code + " tidak ditemukan!"));
                
        return mbtiTypeMapper.toResponseDTO(mbtiType);
    }
}
