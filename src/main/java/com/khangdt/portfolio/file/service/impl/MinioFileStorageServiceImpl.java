package com.khangdt.portfolio.file.service.impl;

import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.file.dto.response.UploadFileResponse;
import com.khangdt.portfolio.file.service.FileStorageService;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.upload.provider", havingValue = "minio")
@Slf4j
public class MinioFileStorageServiceImpl implements FileStorageService {

    @Value("${minio.endpoint:http://localhost:9000}")
    private String endpoint;

    @Value("${minio.public-url:http://localhost:9000}")
    private String publicUrl;

    @Value("${minio.access-key:minioadmin}")
    private String accessKey;

    @Value("${minio.secret-key:minioadmin}")
    private String secretKey;

    @Value("${minio.bucket-name:portfolio-uploads}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO bucket '{}' created successfully.", bucketName);
            }

            // Always ensure public READ policy so browser can load image URLs directly
            String policy = """
            {
              "Version": "2012-10-17",
              "Statement": [
                {
                  "Effect": "Allow",
                  "Principal": {"AWS": ["*"]},
                  "Action": ["s3:GetObject"],
                  "Resource": ["arn:aws:s3:::%s/*"]
                }
              ]
            }
            """.formatted(bucketName);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build()
            );
            log.info("MinIO public read bucket policy applied successfully to '{}'.", bucketName);
        } catch (Exception ex) {
            log.error("Failed to initialize MinIO client or bucket policy", ex);
        }
    }

    @Override
    public UploadFileResponse storeFile(MultipartFile file) {
        if (minioClient == null) {
            throw new BadRequestException("MinIO service is not configured or reachable.");
        }
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new BadRequestException("Only image or PDF files are allowed.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String objectName = UUID.randomUUID().toString() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            String fileUrl;
            if (publicUrl.endsWith("/api/v1/files/raw")) {
                fileUrl = publicUrl + "/" + objectName;
            } else if (publicUrl.contains("/portfolio-uploads")) {
                fileUrl = publicUrl.substring(0, publicUrl.indexOf("/portfolio-uploads")) + "/api/v1/files/raw/" + objectName;
            } else {
                fileUrl = publicUrl + "/api/v1/files/raw/" + objectName;
            }

            return UploadFileResponse.builder()
                    .fileName(objectName)
                    .fileUrl(fileUrl)
                    .fileType(contentType)
                    .size(file.getSize())
                    .build();

        } catch (Exception ex) {
            log.error("Could not store file in MinIO", ex);
            throw new BadRequestException("Could not store file in MinIO. Error: " + ex.getMessage());
        }
    }

    @Override
    public org.springframework.core.io.Resource loadFileAsResource(String filename) {
        if (minioClient == null) {
            throw new BadRequestException("MinIO service is not configured.");
        }
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return new org.springframework.core.io.InputStreamResource(inputStream);
        } catch (Exception ex) {
            log.error("Could not read file from MinIO: {}", filename, ex);
            throw new BadRequestException("File not found in MinIO: " + filename);
        }
    }
}
