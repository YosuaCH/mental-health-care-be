package com.mental.health.care.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Token tidak boleh kosong")
    private String token;

    @NotBlank(message = "Password tidak boleh kosong")
    private String newPassword;
}
