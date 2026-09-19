package com.islamnizami.taskboarddevlab.service;

import com.islamnizami.taskboarddevlab.exception.BadRequestException;
import com.islamnizami.taskboarddevlab.mapper.UserMapper;
import com.islamnizami.taskboarddevlab.model.dto.LoginRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.LoginResponseDTO;
import com.islamnizami.taskboarddevlab.model.dto.RegisterRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.RegisterResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.User;
import com.islamnizami.taskboarddevlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }

        User user = userMapper.toEntity(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());

        return LoginResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
    }
}