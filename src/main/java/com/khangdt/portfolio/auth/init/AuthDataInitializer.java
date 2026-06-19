package com.khangdt.portfolio.auth.init;

import com.khangdt.portfolio.auth.config.DefaultAdminProperties;
import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class AuthDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultAdminProperties defaultAdminProperties;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = User.builder()
                .username(defaultAdminProperties.getUsername())
                .email(defaultAdminProperties.getEmail())
                .fullName("Administrator")
                .password(passwordEncoder.encode(defaultAdminProperties.getPassword()))
                .role("ADMIN")
                .build();

        userRepository.save(admin);
        log.info("Default admin user created: {}", admin.getUsername());
    }
}
