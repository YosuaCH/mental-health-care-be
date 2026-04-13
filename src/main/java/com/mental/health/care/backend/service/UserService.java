package com.mental.health.care.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mental.health.care.backend.dto.ClientCreateDTO;
import com.mental.health.care.backend.dto.PsikiaterCreateDTO;
import com.mental.health.care.backend.dto.UserRequestLoginDTO;
import com.mental.health.care.backend.dto.UpdateProfileDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.mapper.ClientMapper;
import com.mental.health.care.backend.mapper.PsikiaterMapper;
import com.mental.health.care.backend.mapper.UserMapper;
import com.mental.health.care.backend.model.AuthProvider;
import com.mental.health.care.backend.model.BaseUser;
import com.mental.health.care.backend.model.Client;
import com.mental.health.care.backend.model.Psikiater;
import com.mental.health.care.backend.model.Role;
import com.mental.health.care.backend.repository.UserRepository;
import com.mental.health.care.backend.repository.PasswordResetTokenRepository;
import com.mental.health.care.backend.model.PasswordResetToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.UUID;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final PsikiaterMapper psikiaterMapper;

    public void createPasswordResetToken(String email) {
        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email tidak terdaftar!"));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun Google tidak dapat diatur ulang sandinya.");
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .email(email)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(resetToken);
    }

    public String generateResetToken(String email) {
        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email tidak terdaftar!"));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun Google tidak dapat diatur ulang sandinya.");
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .email(email)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(resetToken);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token tidak valid!"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new ResponseStatusException(HttpStatus.GONE, "Token sudah kadaluarsa!");
        }

        BaseUser user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan!"));

        if (user.getPassword().equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sandi baru tidak boleh sama dengan sandi lama!");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }

    public String getUserDisplayName(String email) {
        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email tidak terdaftar!"));

        if (user instanceof Client client) {
            return client.getUsername();
        } else if (user instanceof Psikiater psikiater) {
            return psikiater.getNamaLengkap();
        }
        return "User";
    }

    public UserResponseDTO registerClient(ClientCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sudah terdaftar!");
        }
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username sudah digunakan!");
        }

        Client client = clientMapper.toModel(dto);
        Client savedClient = userRepository.save(client);
        return userMapper.toResponseDTO(savedClient);
    }

    public UserResponseDTO registerPsikiater(PsikiaterCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sudah terdaftar!");
        }

        Psikiater psikiater = psikiaterMapper.toModel(dto);
        if (userRepository.findByNoStr(dto.getNoStr()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor STR sudah terdaftar!");
        }
        Psikiater savedPsikiater = userRepository.save(psikiater);
        return userMapper.toResponseDTO(savedPsikiater);
    }

    public UserResponseDTO login(UserRequestLoginDTO dto) {
        BaseUser user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email tidak ditemukan!"
                ));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Akun ini terdaftar via Google. Gunakan login Google.");
        }

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password salah!");
        }

        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO processGoogleUser(String email, String providerId) {
        Optional<BaseUser> existingUser = userRepository.findByEmail(email);

        BaseUser user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            
            if (user.getAuthProvider() != AuthProvider.GOOGLE) {
                throw new RuntimeException("manual_user");
            }
        
            if (user.getProviderId() == null) {
                user.setProviderId(providerId);
                userRepository.save(user);
            }
        } else {
            user = Client.builder()
                    .email(email)
                    .providerId(providerId)
                    .authProvider(AuthProvider.GOOGLE)
                    .role(Role.CLIENT)
                    .username(generateUniqueUsername(email))
                    .build();
            user = userRepository.save(user);
        }

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO updateProfileFromPrincipal(Object principal, UpdateProfileDTO dto) {
        String userId = null;

        if (principal instanceof UserResponseDTO localUser) {
            userId = localUser.getId();
        } else if (principal instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            BaseUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Google tidak ditemukan!"));
            userId = user.getId();
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User tidak teridentifikasi!");
        }

        return updateProfile(userId, dto);
    }

    public UserResponseDTO updateProfile(String userId, UpdateProfileDTO dto) {
        BaseUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan!"));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            if (user instanceof Client client) {
                client.setUsername(dto.getName());
            } else if (user instanceof Psikiater psikiater) {
                psikiater.setNamaLengkap(dto.getName());
            }
        }

        if (dto.getPicture() != null) {
            user.setPicture(dto.getPicture());
        }

        BaseUser savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(String id) {
        BaseUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan!"));
        return userMapper.toResponseDTO(user);
    }

    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        if (userRepository.findByUsername(baseUsername).isPresent()) {
            return baseUsername + System.currentTimeMillis() % 1000;
        }
        return baseUsername;
    }
}