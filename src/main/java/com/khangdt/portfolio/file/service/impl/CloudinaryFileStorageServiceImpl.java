package com.khangdt.portfolio.file.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.file.dto.response.UploadFileResponse;
import com.khangdt.portfolio.file.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.upload.provider", havingValue = "cloudinary")
public class CloudinaryFileStorageServiceImpl implements FileStorageService {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        if (cloudName == null || cloudName.isBlank()) {
            throw new IllegalStateException("Cloudinary cloud-name property is not configured!");
        }
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public UploadFileResponse storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file.");
        }

        // Kiểm tra xem tệp tin có phải hình ảnh không
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed.");
        }

        try {
            // Upload bytes directly to Cloudinary
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String fileUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            return UploadFileResponse.builder()
                    .fileName(publicId)
                    .fileUrl(fileUrl)
                    .fileType(contentType)
                    .size(file.getSize())
                    .build();

        } catch (IOException ex) {
            throw new BadRequestException("Could not store file on Cloudinary. Error: " + ex.getMessage());
        }
    }
}
