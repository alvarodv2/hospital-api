package com.hospital.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("JwtAuthFilter - Unit Tests")
@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;
    private String token;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        token = "valid.test.token";
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should process request with valid token")
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(jwtUtil.extractRole(token)).thenReturn("USER");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should process request with invalid token")
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtil.isTokenValid(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should process request without authorization header")
    void doFilterInternal_WithoutAuthHeader_ShouldContinueChain() throws ServletException, IOException {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).isTokenValid(anyString());
    }

    @Test
    @DisplayName("Should process request with invalid authorization header format")
    void doFilterInternal_WithInvalidHeaderFormat_ShouldContinueChain() throws ServletException, IOException {
        request.addHeader("Authorization", "InvalidFormat " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).isTokenValid(anyString());
    }


}