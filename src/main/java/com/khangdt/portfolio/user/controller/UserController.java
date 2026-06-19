package com.khangdt.portfolio.user.controller;

import com.khangdt.portfolio.auth.dto.response.AuthUserResponse;
import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.user.dto.UserUpdateProfileRequest;
import com.khangdt.portfolio.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile management APIs")
public class UserController {

    private static final String BEARER_AUTH = "Bearer Authentication";
    private final UserService userService;

    @Operation(
            summary = "Get current user profile",
            description = "Retrieves profile details of the currently logged-in user."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AuthUserResponse>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        AuthUserResponse response = userService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    @Operation(
            summary = "Update current user profile",
            description = "Updates profile details of the currently logged-in user."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<AuthUserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateProfileRequest request
    ) {
        AuthUserResponse response = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }
}
