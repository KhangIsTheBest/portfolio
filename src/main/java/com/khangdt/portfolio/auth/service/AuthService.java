package com.khangdt.portfolio.auth.service;

import com.khangdt.portfolio.auth.config.JwtProperties;
import com.khangdt.portfolio.auth.dto.request.LoginRequest;
import com.khangdt.portfolio.auth.dto.response.AuthResponse;
import com.khangdt.portfolio.auth.dto.response.AuthUserResponse;
import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.auth.security.CustomUserDetails;
import com.khangdt.portfolio.auth.security.JwtService;
import com.khangdt.portfolio.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(userDetails);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtProperties.getExpirationMs() / 1000)
                    .user(toAuthUserResponse(userDetails.getUser()))
                    .build();
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    private AuthUserResponse toAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
