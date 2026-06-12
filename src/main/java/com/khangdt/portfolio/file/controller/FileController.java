package com.khangdt.portfolio.file.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.file.dto.response.UploadFileResponse;
import com.khangdt.portfolio.file.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Files", description = "File upload management APIs")
public class FileController {

    private static final String BEARER_AUTH = "Bearer Authentication";

    private final FileStorageService fileStorageService;

    @Operation(
            summary = "Upload image file",
            description = "Uploads an image file to the server storage. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "File uploaded successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid file or non-image uploaded"
            )
    })
    @PostMapping(value = "/api/v1/admin/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadFileResponse>> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        UploadFileResponse response = fileStorageService.storeFile(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("File uploaded successfully", response));
    }
}
