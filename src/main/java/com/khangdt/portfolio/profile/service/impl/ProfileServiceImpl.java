package com.khangdt.portfolio.profile.service.impl;

import com.khangdt.portfolio.profile.dto.request.ProfileUpdateRequest;
import com.khangdt.portfolio.profile.dto.response.ProfileResponse;
import com.khangdt.portfolio.profile.entity.Profile;
import com.khangdt.portfolio.profile.mapper.ProfileMapper;
import com.khangdt.portfolio.profile.repository.ProfileRepository;
import com.khangdt.portfolio.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    @Transactional
    @Cacheable(value = "profile", key = "'default'")
    public ProfileResponse getProfile() {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> profileRepository.save(Profile.builder()
                        .fullName("Phan Duy Khang")
                        .title("Lập trình viên Backend / Full-Stack")
                        .aboutMe("Sinh viên ngành Kỹ thuật phần mềm với nền tảng tốt về Data Structures & Algorithms cùng khả năng tự học tốt. Mong muốn phát triển chuyên sâu trong lĩnh vực Backend Engineering, hướng đến việc xây dựng các hệ thống hiệu năng cao.")
                        .email("pdkhang.dev@gmail.com")
                        .githubUrl("https://github.com/KhangIsTheBest")
                        .linkedinUrl("https://linkedin.com/in/phanduykhang")
                        .build()));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    @CacheEvict(value = "profile", allEntries = true)
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        Profile profile = profileRepository.findFirstByOrderByIdAsc()
                .orElseGet(Profile::new);

        profileMapper.updateEntity(profile, request);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(savedProfile);
    }
}
