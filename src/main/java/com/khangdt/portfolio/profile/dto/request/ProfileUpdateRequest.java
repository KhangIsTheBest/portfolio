package com.khangdt.portfolio.profile.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String aboutMe;

    @Size(max = 500, message = "GitHub URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "GitHub URL must be empty or a valid http(s) URL"
    )
    private String githubUrl;

    @Size(max = 500, message = "LinkedIn URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "LinkedIn URL must be empty or a valid http(s) URL"
    )
    private String linkedinUrl;

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "Avatar URL must be empty or a valid http(s) URL"
    )
    private String avatarUrl;
}
