package com.khangdt.portfolio.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth.default-admin")
public class DefaultAdminProperties {

    private String username = "admin";
    private String email = "admin@portfolio.local";
    private String password = "admin123";
}
