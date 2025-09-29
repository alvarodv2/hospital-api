package com.hospital.api.security;

import com.hospital.api.entity.User;
import com.hospital.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AuthService - Unit Tests")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    private String mockToken;

    @BeforeEach
    void setUp() {
        mockToken = "mock.jwt.token";
        
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setRole("USER");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        user = User.builder()
                .username("testuser")
                .password("encoded_password")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Register - Should successfully register new user")
    void register_ShouldRegisterNewUser() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(registerRequest.getUsername(), "USER")).thenReturn(mockToken);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register - Should throw exception when username exists")
    void register_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Login - Should successfully authenticate user")
    void login_ShouldAuthenticateUser() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getUsername(), user.getRole())).thenReturn(mockToken);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
    }

    @Test
    @DisplayName("Login - Should throw exception when user not found")
    void login_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Login - Should throw exception when password is invalid")
    void login_WhenPasswordInvalid_ShouldThrowException() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }


}