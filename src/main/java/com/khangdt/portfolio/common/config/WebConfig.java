package com.khangdt.portfolio.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        // Phục vụ ảnh tĩnh từ thư mục vật lý lưu trữ ngoài classpath
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath.toString() + "/");
    }
}
