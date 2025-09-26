package com.hospital.api.service;

import com.hospital.api.entity.User;
import com.hospital.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = User.builder()
                .id(1L)
                .username("john_doe")
                .password("secret")
                .role("USER")
                .build();
    }

    @Test
    void saveUser_ShouldSaveAndReturnUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User created = userService.saveUser(user1);

        assertThat(created).isEqualTo(user1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void getByUsername_WhenExists_ShouldReturnUser() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user1));

        Optional<User> found = userService.getByUsername("john_doe");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_doe");
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void getByUsername_WhenNotFound_ShouldReturnEmpty() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> found = userService.getByUsername("unknown");

        assertThat(found).isEmpty();
        verify(userRepository, times(1)).findByUsername("unknown");
    }

    @Test
    void getById_WhenExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> found = userService.getById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_WhenNotFound_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> found = userService.getById(1L);

        assertThat(found).isEmpty();
        verify(userRepository, times(1)).findById(1L);
    }


}
