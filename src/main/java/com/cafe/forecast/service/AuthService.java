package com.cafe.forecast.service;

import com.cafe.forecast.dto.LoginRequest;
import com.cafe.forecast.dto.LoginResponse;
import com.cafe.forecast.dto.RegisterRequest;
import com.cafe.forecast.exception.ValidationException;
import com.cafe.forecast.model.User;
import com.cafe.forecast.repository.UserRepository;
import com.cafe.forecast.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public LoginResponse authenticate(LoginRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));


        String token = jwtUtil.generateToken(user);


        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .expiresIn(86400000L)
                .build();
    }


    public LoginResponse register(RegisterRequest request) {


        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username já está em uso");
        }


        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();


        userRepository.save(user);


        return authenticate(LoginRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build());
    }
}
