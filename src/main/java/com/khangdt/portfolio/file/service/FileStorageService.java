package com.khangdt.portfolio.file.service;

import com.khangdt.portfolio.file.dto.response.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    UploadFileResponse storeFile(MultipartFile file);
    Resource loadFileAsResource(String filename);
}
