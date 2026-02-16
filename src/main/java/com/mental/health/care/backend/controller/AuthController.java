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
import com.mental.health.care.backend.dto.WebResponseLoginDTO;
import com.mental.health.care.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/register/client")
    public WebResponseLoginDTO registerClient(@RequestBody ClientCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerClient(dto);
        return WebResponseLoginDTO.builder()
                .message("Registrasi Client Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/register/psikiater")
    public WebResponseLoginDTO registerPsikiater(@RequestBody PsikiaterCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerPsikiater(dto);
        return WebResponseLoginDTO.builder()
                .message("Registrasi Psikiater Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/login")
    public WebResponseLoginDTO login(@RequestBody UserRequestLoginDTO dto) {
        UserResponseDTO userResponse = userService.login(dto);

        String nameToShow = (userResponse.getUsername() != null) 
                            ? userResponse.getUsername() 
                            : userResponse.getNamaLengkap();

        return WebResponseLoginDTO.builder()
                .message("Login Berhasil, Selamat Datang " + nameToShow)
                .data(userResponse)
                .build();
    }
}
