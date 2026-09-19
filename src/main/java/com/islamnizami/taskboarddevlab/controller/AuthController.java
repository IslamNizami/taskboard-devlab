package com.islamnizami.taskboarddevlab.controller;


import com.islamnizami.taskboarddevlab.model.dto.LoginRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.LoginResponseDTO;
import com.islamnizami.taskboarddevlab.model.dto.RegisterRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.RegisterResponseDTO;
import com.islamnizami.taskboarddevlab.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        RegisterResponseDTO registerResponseDTO = authService.register(registerRequestDTO);
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
