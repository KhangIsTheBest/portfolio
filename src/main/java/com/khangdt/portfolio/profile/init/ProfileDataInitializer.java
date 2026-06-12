package com.khangdt.portfolio.profile.init;

import com.khangdt.portfolio.profile.entity.Profile;
import com.khangdt.portfolio.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class ProfileDataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;

    @Override
    public void run(String... args) {
        if (profileRepository.count() > 0) {
            return;
        }

        profileRepository.save(Profile.builder().build());
        log.info("Default empty profile created");
    }
}
