package com.mental.health.care.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsikiaterCreateDTO {
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    @NotBlank(message = "Nomor STR wajib diisi")
    private String noStr;

    @NotBlank(message = "Nama lengkap wajib diisi")
    private String namaLengkap;

    @NotBlank(message = "Nomor WhatsApp wajib diisi")
    private String nomorWa;
}
