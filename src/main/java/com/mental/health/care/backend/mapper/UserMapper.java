package com.mental.health.care.backend.mapper;

import org.springframework.stereotype.Component;

import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.model.BaseUser;
import com.mental.health.care.backend.model.Psikiater;

import lombok.RequiredArgsConstructor;

import com.mental.health.care.backend.model.Client;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ClientMapper clientMapper;
    private final PsikiaterMapper psikiaterMapper;

    public UserResponseDTO toResponseDTO(BaseUser user) {
        if (user instanceof Client client) {
            return clientMapper.toResponseDTO(client); 
        } 
        else if (user instanceof Psikiater psikiater) {
            return psikiaterMapper.toResponseDTO(psikiater); 
        }
        return null;
    }
}