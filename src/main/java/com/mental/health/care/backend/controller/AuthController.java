package com.mental.health.care.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mental.health.care.backend.dto.*;
import com.mental.health.care.backend.service.UserService;
import com.mental.health.care.backend.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final EmailService emailService;
    
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @PostMapping("/forgot-password")
    public WebResponseDTO forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        String token = userService.generateResetToken(dto.getEmail());
        String name = userService.getUserDisplayName(dto.getEmail());
        String resetLink = frontendUrl + "/reset_password.html?token=" + token;

        try {
            emailService.sendResetPasswordEmail(dto.getEmail(), name, resetLink);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengirim email: " + e.getMessage());
        }

        return WebResponseDTO.builder()
                .message("Tautan pemulihan telah dikirim ke email Anda.")
                .build();
    }

    @PostMapping("/reset-password")
    public WebResponseDTO resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        userService.resetPassword(dto.getToken(), dto.getNewPassword());
        return WebResponseDTO.builder()
                .message("Kata sandi berhasil diperbarui. Silakan login kembali.")
                .build();
    }

    @PostMapping("/register/client")
    public WebResponseDTO registerClient(@RequestBody ClientCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerClient(dto);
        try {
            emailService.sendWelcomeEmail(dto.getEmail(), dto.getUsername());
        } catch (MessagingException e) {
            System.err.println("Gagal mengirim email selamat datang: " + e.getMessage());
        }
        return WebResponseDTO.builder()
                .message("Registrasi Client Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/register/psikiater")
    public WebResponseDTO registerPsikiater(@RequestBody PsikiaterCreateDTO dto) {
        UserResponseDTO userResponse = userService.registerPsikiater(dto);
        try {
            emailService.sendWelcomeMitraEmail(dto.getEmail(), dto.getNamaLengkap());
        } catch (MessagingException e) {
            System.err.println("Gagal mengirim email selamat datang mitra: " + e.getMessage());
        }
        return WebResponseDTO.builder()
                .message("Registrasi Psikiater Berhasil")
                .data(userResponse)
                .build();
    }

    @PostMapping("/login")
    public WebResponseDTO login(@RequestBody UserRequestLoginDTO dto, HttpServletRequest request) {
        UserResponseDTO userResponse = userService.login(dto);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userResponse, null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                            SecurityContextHolder.getContext());

        return WebResponseDTO.builder()
                .data(userResponse)
                .build();
    }

    @GetMapping("/me")
    public WebResponseDTO getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User belum login");
        }

        UserResponseDTO userResponse;

        if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            String providerId = oauth2User.getAttribute("sub");
            userResponse = userService.processGoogleUser(email, providerId);
        } 
        else if (principal instanceof UserResponseDTO localUser) {
            userResponse = userService.getUserById(localUser.getId());
        } 
        else {
            return WebResponseDTO.builder()
                    .message("Data user tidak dikenal")
                    .data(null)
                    .build();
        }

        return WebResponseDTO.builder()
                .message("Berhasil mengambil data user")
                .data(userResponse)
                .build();
    }
}
