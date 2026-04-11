package com.mental.health.care.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email salah")
    private String email;
}
