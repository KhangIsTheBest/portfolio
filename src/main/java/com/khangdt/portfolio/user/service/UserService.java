package com.khangdt.portfolio.user.service;

import com.khangdt.portfolio.auth.dto.response.AuthUserResponse;
import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.auth.repository.UserRepository;
import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.common.exception.DuplicateResourceException;
import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.user.dto.UserUpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toAuthUserResponse(user);
    }

    @Transactional
    public AuthUserResponse updateProfile(String username, UserUpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // If email changed, check if new email is already in use
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateResourceException("User", "email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        user.setFullName(request.getFullName());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new BadRequestException("Current password is required to set a new password");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Current password does not match");
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return toAuthUserResponse(savedUser);
    }

    private AuthUserResponse toAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }
}
