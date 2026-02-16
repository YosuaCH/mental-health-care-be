package com.mental.health.care.backend.mapper;

import com.mental.health.care.backend.dto.ClientCreateDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.model.Client;
import com.mental.health.care.backend.model.Role;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toModel(ClientCreateDTO dto) {
        return Client.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.CLIENT)
                .build();
    }

    public UserResponseDTO toResponseDTO(Client user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }
}