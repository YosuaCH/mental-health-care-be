package com.mental.health.care.backend.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.care.backend.dto.UpdateProfileDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/profile")
    public UserResponseDTO updateProfile(
            @AuthenticationPrincipal Object principal,
            @RequestBody UpdateProfileDTO dto) {
        
        return userService.updateProfileFromPrincipal(principal, dto);
    }

    @GetMapping("/getAll")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
