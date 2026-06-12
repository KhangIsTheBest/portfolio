package com.khangdt.portfolio.profile.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.profile.dto.request.ProfileUpdateRequest;
import com.khangdt.portfolio.profile.dto.response.ProfileResponse;
import com.khangdt.portfolio.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Portfolio profile APIs")
public class ProfileController {

    private static final String BEARER_AUTH = "Bearer Authentication";

    private final ProfileService profileService;

    @Operation(
            summary = "Get profile",
            description = "Returns the portfolio owner profile"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Profile not found"
            )
    })
    @GetMapping("/api/v1/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
        ProfileResponse profile = profileService.getProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @Operation(
            summary = "Update profile",
            description = "Creates or updates the portfolio profile. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully"
            )
    })
    @PutMapping("/api/v1/admin/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProfileUpdateRequest.class))
            )
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        ProfileResponse profile = profileService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }
}
