package com.hospital.api.exceptions;

import com.hospital.api.exception.auth.AuthException;
import com.hospital.api.exception.conflict.ConflictException;
import com.hospital.api.exception.handler.GlobalExceptionHandler;
import com.hospital.api.exception.invalid.InvalidException;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler - Clean Tests")
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle ResourceNotFoundException correctly")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = ResourceNotFoundException.Builder.appointment("123");
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ResourceNotFoundException.ApiErrorResponse> response =
                globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("APPOINTMENT_404", response.getBody().getErrorCode());
        assertEquals("Appointment", response.getBody().getResourceType());
        assertEquals("123", response.getBody().getResourceId());
    }

    @Test
    @DisplayName("Should handle InvalidException correctly")
    void shouldHandleInvalidException() {
        InvalidException exception = new InvalidException("Invalid email format");
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<InvalidException.ApiErrorResponse> response =
                globalExceptionHandler.handleInvalidException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should handle AuthException correctly")
    void shouldHandleAuthException() {
        AuthException exception = new AuthException("Invalid credentials");
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<AuthException.ApiErrorResponse> response =
                globalExceptionHandler.handleAuthException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should handle ConflictException correctly")
    void shouldHandleConflictException() {
        ConflictException exception = new ConflictException("Resource already exists");
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<ConflictException.ApiErrorResponse> response =
                globalExceptionHandler.handleConflictException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException correctly")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        FieldError fieldError1 = new FieldError("user", "email", "Email is required");
        FieldError fieldError2 = new FieldError("user", "name", "Name must not be blank");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleValidationExceptions(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("Validation Failed", body.get("error"));
        assertEquals("/api/test", body.get("path"));
        assertNotNull(body.get("timestamp"));

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals("Email is required", errors.get("email"));
        assertEquals("Name must not be blank", errors.get("name"));
    }

    @Test
    @DisplayName("Should handle AuthenticationException correctly")
    void shouldHandleAuthenticationException() {
        AuthenticationException exception = new AuthenticationException("Invalid credentials") {};
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/auth");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleAuthenticationException(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(401, body.get("status"));
        assertEquals("Authentication Failed", body.get("error"));
        assertEquals("Invalid credentials", body.get("message"));
        assertEquals("/api/auth", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Should handle AccessDeniedException correctly")
    void shouldHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/admin");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleAccessDeniedException(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(403, body.get("status"));
        assertEquals("Access Denied", body.get("error"));
        assertEquals("Access denied", body.get("message"));
        assertEquals("/api/admin", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/error");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleAllUncaughtException(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
        assertEquals("/api/error", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void shouldHandleExceptionWithNullMessage() {
        Exception exception = new RuntimeException((String) null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/null-error");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleAllUncaughtException(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Should handle multiple validation errors")
    void shouldHandleMultipleValidationErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        List<FieldError> fieldErrors = List.of(
                new FieldError("user", "email", "Email is required"),
                new FieldError("user", "password", "Password too short"),
                new FieldError("user", "age", "Must be positive")
        );

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn((List) fieldErrors);
        when(request.getRequestURI()).thenReturn("/api/users");

        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleValidationExceptions(exception, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertEquals(3, errors.size());
        assertEquals("Email is required", errors.get("email"));
        assertEquals("Password too short", errors.get("password"));
        assertEquals("Must be positive", errors.get("age"));
    }

    @Test
    @DisplayName("Should maintain consistent response structure")
    void shouldMaintainConsistentResponseStructure() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/consistency-test");

        Exception runtimeException = new RuntimeException("Runtime error");
        ResponseEntity<Map<String, Object>> runtimeResponse =
                globalExceptionHandler.handleAllUncaughtException(runtimeException, request);

        AuthenticationException authException = new AuthenticationException("Auth error") {};
        ResponseEntity<Map<String, Object>> authResponse =
                globalExceptionHandler.handleAuthenticationException(authException, request);

        AccessDeniedException accessException = new AccessDeniedException("Access error");
        ResponseEntity<Map<String, Object>> accessResponse =
                globalExceptionHandler.handleAccessDeniedException(accessException, request);

        assertResponseStructure(runtimeResponse.getBody());
        assertResponseStructure(authResponse.getBody());
        assertResponseStructure(accessResponse.getBody());
    }

    private void assertResponseStructure(Map<String, Object> responseBody) {
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("timestamp"));
        assertTrue(responseBody.containsKey("status"));
        assertTrue(responseBody.containsKey("error"));
        assertTrue(responseBody.containsKey("path"));

        String timestamp = (String) responseBody.get("timestamp");
        assertNotNull(timestamp);
        assertFalse(timestamp.isEmpty());

        assertTrue(responseBody.get("status") instanceof Integer);
        assertTrue(responseBody.get("path") instanceof String);
    }

}