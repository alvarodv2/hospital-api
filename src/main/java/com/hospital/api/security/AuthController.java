package com.hospital.api.security;

import com.hospital.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    Logger logger = Logger.getLogger(AuthController.class.getName());

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        logger.info("User register done ✅");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login done ✅");
        return ResponseEntity.ok(authService.login(request));
    }

}

