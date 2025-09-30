package com.hospital.api.security;

import com.hospital.api.dto.*;
import com.hospital.api.entity.User;
import com.hospital.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    public AuthResponse register(RegisterRequest request) {
        logger.info("Attempting to register new user: " + request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warning("Registration failed: Username already exists: " + request.getUsername());
            throw new RuntimeException("User already exists");
        }

        String role = (request.getRole() != null) ? request.getRole().toUpperCase() : "USER";

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        logger.info("User role from database: " + user.getRole());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        logger.info("Generated token with role: " + user.getRole());

        return new AuthResponse(token);
    }

}
