package com.hospital.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("SecurityConfig - Unit Tests")
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private AuthenticationConfiguration authConfig;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthFilter);
    }

    @Test
    @DisplayName("Should create BCryptPasswordEncoder")
    void passwordEncoder_ShouldCreateBCryptPasswordEncoder() {
        var encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    @DisplayName("Should create AuthenticationManager")
    void authenticationManager_ShouldCreateAuthenticationManager() throws Exception {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(mockAuthManager);

        var authManager = securityConfig.authenticationManager(authConfig);

        assertNotNull(authManager);
        assertEquals(mockAuthManager, authManager);
    }

    @Test
    @DisplayName("Should configure SecurityFilterChain")
    void filterChain_ShouldConfigureSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_SELF);
        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(filterChain);

        // Mock los métodos de configuración
        doAnswer(invocation -> http).when(http).csrf(any());
        doAnswer(invocation -> http).when(http).sessionManagement(any());
        doAnswer(invocation -> http).when(http).authorizeHttpRequests(any());

        doAnswer(invocation -> {
            return http;
        }).when(http).addFilterBefore(any(), any());

        var result = securityConfig.filterChain(http);

        assertNotNull(result);
        verify(http).csrf(any());
        verify(http).sessionManagement(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).addFilterBefore(eq(jwtAuthFilter), any());
        verify(http).build();
    }



}