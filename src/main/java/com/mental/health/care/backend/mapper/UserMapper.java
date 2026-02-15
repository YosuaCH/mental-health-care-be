package com.mental.health.care.backend.mapper;

import com.mental.health.care.backend.dto.UserCreateDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.model.User;
import com.mental.health.care.backend.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel(UserCreateDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.USER)
                .build();
    }

    public UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }
}