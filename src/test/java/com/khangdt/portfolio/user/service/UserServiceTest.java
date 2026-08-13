package com.khangdt.portfolio.user.service;

import com.khangdt.portfolio.auth.dto.response.AuthUserResponse;
import com.khangdt.portfolio.auth.entity.Role;
import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.auth.repository.UserRepository;
import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.user.dto.UserUpdateProfileRequest;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .fullName("John Doe")
                .password("encoded_old_password")
                .role(Role.USER)
                .build();
    }

    @Test
    void updateProfile_WithoutPasswordChange_Success() {
        UserUpdateProfileRequest request = UserUpdateProfileRequest.builder()
                .email("john@example.com")
                .fullName("John updated")
                .build();

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        AuthUserResponse response = userService.updateProfile("john_doe", request);

        assertNotNull(response);
        assertEquals("John updated", response.getFullName());
    }

    @Test
    void updateProfile_WithPasswordChange_WrongCurrentPassword_ThrowsException() {
        UserUpdateProfileRequest request = UserUpdateProfileRequest.builder()
                .email("john@example.com")
                .fullName("John Doe")
                .currentPassword("wrong_old")
                .password("new_password_123")
                .build();

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong_old", "encoded_old_password")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.updateProfile("john_doe", request));
    }
}
