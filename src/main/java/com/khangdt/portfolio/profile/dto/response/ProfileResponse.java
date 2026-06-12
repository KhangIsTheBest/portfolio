package com.khangdt.portfolio.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String fullName;
    private String title;
    private String aboutMe;
    private String githubUrl;
    private String linkedinUrl;
    private String email;
    private String avatarUrl;
    private LocalDateTime updatedAt;
}
