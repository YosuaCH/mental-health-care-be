package com.mental.health.care.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.dto.ClientCreateDTO;
import com.mental.health.care.backend.dto.PsikiaterCreateDTO;
import com.mental.health.care.backend.dto.UserRequestLoginDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.dto.WebResponseDTO;
import com.mental.health.care.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/register/client")
    public WebResponseDTO registerClient(@RequestBody ClientCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerClient(dto);
        return WebResponseDTO.builder()
                .message("Registrasi Client Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/register/psikiater")
    public WebResponseDTO registerPsikiater(@RequestBody PsikiaterCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerPsikiater(dto);
        return WebResponseDTO.builder()
                .message("Registrasi Psikiater Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/login")
    public WebResponseDTO login(@RequestBody UserRequestLoginDTO dto) {
        UserResponseDTO userResponse = userService.login(dto);

        String nameToShow = (userResponse.getUsername() != null) 
                            ? userResponse.getUsername() 
                            : userResponse.getNamaLengkap();

        return WebResponseDTO.builder()
                .message("Login Berhasil, Selamat Datang " + nameToShow)
                .data(userResponse)
                .build();
    }
}
