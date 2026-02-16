package com.mental.health.care.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.dto.UserCreateDTO;
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

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserCreateDTO dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public WebResponseLoginDTO login(@RequestBody UserRequestLoginDTO dto) {
        UserResponseDTO userResponse = userService.login(dto);

        return WebResponseLoginDTO.builder()
                .message("Login Berhasil, Selamat Datang " + userResponse.getUsername())
                .data(userResponse)
                .build();
    }
}
