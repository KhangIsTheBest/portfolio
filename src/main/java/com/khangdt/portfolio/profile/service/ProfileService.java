package com.khangdt.portfolio.profile.service;

import com.khangdt.portfolio.profile.dto.request.ProfileUpdateRequest;
import com.khangdt.portfolio.profile.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile();

    ProfileResponse updateProfile(ProfileUpdateRequest request);
}
