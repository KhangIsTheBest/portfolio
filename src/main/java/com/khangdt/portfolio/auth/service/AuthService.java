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
import com.khangdt.portfolio.auth.dto.request.RegisterRequest;
import com.khangdt.portfolio.auth.dto.request.GoogleLoginRequest;
import com.khangdt.portfolio.auth.repository.UserRepository;
import com.khangdt.portfolio.common.exception.DuplicateResourceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.google.client-id:}")
    private String googleClientId;

    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        User savedUser = userRepository.save(user);
        return toAuthUserResponse(savedUser);
    }

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

    @Transactional
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new UnauthorizedException("Google Client ID is not configured on the server");
        }
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new UnauthorizedException("Invalid Google ID Token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            if (name == null || name.isBlank()) {
                name = email.split("@")[0];
            }

            // Find or create user
            String finalName = name;
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                String prefix = email.split("@")[0];
                String username = prefix;
                int count = 1;
                while (userRepository.findByUsername(username).isPresent()) {
                    username = prefix + count;
                    count++;
                }

                User newUser = User.builder()
                        .username(username)
                        .email(email)
                        .fullName(finalName)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .role("USER")
                        .build();
                return userRepository.save(newUser);
            });

            CustomUserDetails userDetails = new CustomUserDetails(user);
            String accessToken = jwtService.generateToken(userDetails);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtProperties.getExpirationMs() / 1000)
                    .user(toAuthUserResponse(user))
                    .build();
        } catch (Exception ex) {
            throw new UnauthorizedException("Google authentication failed: " + ex.getMessage());
        }
    }

    private AuthUserResponse toAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}
