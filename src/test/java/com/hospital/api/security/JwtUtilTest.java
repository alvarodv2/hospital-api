package com.hospital.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil - Unit Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testSecret;
    private long testExpiration;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        testSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        testExpiration = 3600000;

        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    @DisplayName("Should generate valid token with username and role")
    void generateToken_ShouldCreateValidToken() {
        String username = "testuser";
        String role = "USER";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals(username, jwtUtil.extractUsername(token));
        assertEquals(role, jwtUtil.extractRole(token));
    }

    @Test
    @DisplayName("Should extract correct username from token")
    void extractUsername_ShouldReturnCorrectUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username, "USER");

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Should extract correct role from token")
    void extractRole_ShouldReturnCorrectRole() {
        String role = "ADMIN";
        String token = jwtUtil.generateToken("testuser", role);

        String extractedRole = jwtUtil.extractRole(token);

        assertEquals(role, extractedRole);
    }

    @Test
    @DisplayName("Should validate token correctly")
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("testuser", "USER");

        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void isTokenValid_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.string";

        assertFalse(jwtUtil.isTokenValid(invalidToken));
    }


}