package com.cafe.forecast.controller;

import com.cafe.forecast.dto.ApiResponse;
import com.cafe.forecast.dto.LoginRequest;
import com.cafe.forecast.dto.LoginResponse;
import com.cafe.forecast.dto.RegisterRequest;
import com.cafe.forecast.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de login e registro")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.authenticate(request);

        return ResponseEntity.ok(
                ApiResponse.ok(response, "Login realizado com sucesso")
        );
    }


    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        LoginResponse response = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.ok(response, "Usuário registrado com sucesso")
        );
    }
}
