package com.mental.health.care.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mental.health.care.backend.dto.ClientCreateDTO;
import com.mental.health.care.backend.dto.PsikiaterCreateDTO;
import com.mental.health.care.backend.dto.UserRequestLoginDTO;
import com.mental.health.care.backend.dto.UserResponseDTO;
import com.mental.health.care.backend.mapper.ClientMapper;
import com.mental.health.care.backend.mapper.PsikiaterMapper;
import com.mental.health.care.backend.mapper.UserMapper;
import com.mental.health.care.backend.model.BaseUser;
import com.mental.health.care.backend.model.Client;
import com.mental.health.care.backend.model.Psikiater;
import com.mental.health.care.backend.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final PsikiaterMapper psikiaterMapper;

    public UserResponseDTO registerClient(ClientCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sudah terdaftar!");
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
}