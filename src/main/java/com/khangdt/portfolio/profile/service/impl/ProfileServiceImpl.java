package com.khangdt.portfolio.profile.service.impl;

import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.profile.dto.request.ProfileUpdateRequest;
import com.khangdt.portfolio.profile.dto.response.ProfileResponse;
import com.khangdt.portfolio.profile.entity.Profile;
import com.khangdt.portfolio.profile.mapper.ProfileMapper;
import com.khangdt.portfolio.profile.repository.ProfileRepository;
import com.khangdt.portfolio.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponse getProfile() {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseGet(Profile::new);

        profileMapper.updateEntity(profile, request);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(savedProfile);
    }
}
