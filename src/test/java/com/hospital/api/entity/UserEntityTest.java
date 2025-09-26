package com.hospital.api.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testBuilderAndGetters() {
        User user = User.builder()
                .username("john_doe")
                .password("secret")
                .role("USER")
                .build();

        assertEquals("john_doe", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass123");
        user.setRole("ADMIN");

        assertEquals("alice", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }




}
