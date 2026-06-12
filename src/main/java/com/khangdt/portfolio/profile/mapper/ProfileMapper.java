package com.khangdt.portfolio.profile.mapper;

import com.khangdt.portfolio.profile.dto.request.ProfileUpdateRequest;
import com.khangdt.portfolio.profile.dto.response.ProfileResponse;
import com.khangdt.portfolio.profile.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public void updateEntity(Profile profile, ProfileUpdateRequest request) {
        profile.setFullName(normalizeText(request.getFullName()));
        profile.setTitle(normalizeText(request.getTitle()));
        profile.setAboutMe(normalizeText(request.getAboutMe()));
        profile.setGithubUrl(normalizeUrl(request.getGithubUrl()));
        profile.setLinkedinUrl(normalizeUrl(request.getLinkedinUrl()));
        profile.setEmail(normalizeText(request.getEmail()));
        profile.setAvatarUrl(normalizeUrl(request.getAvatarUrl()));
    }

    public ProfileResponse toResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .title(profile.getTitle())
                .aboutMe(profile.getAboutMe())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .email(profile.getEmail())
                .avatarUrl(profile.getAvatarUrl())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        return url.trim();
    }
}
