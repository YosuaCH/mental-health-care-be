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

import com.mental.health.care.backend.dto.ClientCreateDTO;
import com.mental.health.care.backend.dto.PsikiaterCreateDTO;
import com.mental.health.care.backend.dto.UserRequestLoginDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.dto.WebResponseDTO;
import com.mental.health.care.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
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
    public WebResponseDTO login(@RequestBody UserRequestLoginDTO dto, HttpServletRequest request) {
        UserResponseDTO userResponse = userService.login(dto);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userResponse, null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                            SecurityContextHolder.getContext());

        String nameToShow = (userResponse.getUsername() != null) 
                            ? userResponse.getUsername() 
                            : userResponse.getNamaLengkap();

        return WebResponseDTO.builder()
                .message("Login Berhasil, Selamat Datang " + nameToShow)
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
            userResponse = localUser;
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
