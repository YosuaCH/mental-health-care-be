package com.mental.health.care.backend.mapper;

import com.mental.health.care.backend.dto.UserResponseDTO;

import org.springframework.stereotype.Component;

import com.mental.health.care.backend.dto.PsikiaterCreateDTO;
import com.mental.health.care.backend.model.Psikiater;
import com.mental.health.care.backend.model.Role;

@Component
public class PsikiaterMapper {
    public Psikiater toModel(PsikiaterCreateDTO dto) {
        return Psikiater.builder()
                .namaLengkap(dto.getNamaLengkap())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .noStr(dto.getNoStr())
                .role(Role.PSIKIATER)
                .nomorWa(dto.getNomorWa())
                .build();
    }

    public UserResponseDTO toResponseDTO(Psikiater psikiater) {
        return UserResponseDTO.builder()
                .id(psikiater.getId())
                .namaLengkap(psikiater.getNamaLengkap())
                .email(psikiater.getEmail())
                .role(psikiater.getRole() != null ? psikiater.getRole().name() : "PSIKIATER")
                .noStr(psikiater.getNoStr())
                .nomorWa(psikiater.getNomorWa())
                .build();
    }
}
