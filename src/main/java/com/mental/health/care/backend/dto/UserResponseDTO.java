package com.mental.health.care.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String email;
    private String role;
    private String authProvider;

    private String username;    
    private String namaLengkap;
    private String noStr;
    private String nomorWa;
}