package com.khangdt.portfolio.file.service.impl;

import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.file.dto.response.UploadFileResponse;
import com.khangdt.portfolio.file.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.upload.provider", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new BadRequestException("Could not create the directory where the uploaded files will be stored: " + ex.getMessage());
        }
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

        // Làm sạch tên file và kiểm tra an toàn
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) {
            throw new BadRequestException("Filename contains invalid path sequence: " + originalFileName);
        }

        // Tạo tên file duy nhất bằng UUID để tránh trùng lặp
        String extension = "";
        int extIndex = originalFileName.lastIndexOf('.');
        if (extIndex > 0) {
            extension = originalFileName.substring(extIndex);
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Đường dẫn tương đối để truy cập ảnh tĩnh
            String fileUrl = "/uploads/" + uniqueFileName;

            return UploadFileResponse.builder()
                    .fileName(uniqueFileName)
                    .fileUrl(fileUrl)
                    .fileType(contentType)
                    .size(file.getSize())
                    .build();

        } catch (IOException ex) {
            throw new BadRequestException("Could not store file " + originalFileName + ". Please try again! Error: " + ex.getMessage());
        }
    }
}
