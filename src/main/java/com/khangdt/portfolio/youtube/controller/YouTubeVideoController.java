package com.khangdt.portfolio.youtube.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoCreateRequest;
import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoUpdateRequest;
import com.khangdt.portfolio.youtube.dto.response.YouTubeVideoResponse;
import com.khangdt.portfolio.youtube.service.YouTubeVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "YouTube Videos", description = "YouTube video showcase and management APIs")
public class YouTubeVideoController {

    private static final String BEARER_AUTH = "Bearer Authentication";
    private final YouTubeVideoService videoService;

    @Operation(summary = "Get all YouTube videos", description = "Retrieves public list of YouTube videos, optionally filtered by category")
    @GetMapping("/api/v1/youtube/videos")
    public ResponseEntity<ApiResponse<List<YouTubeVideoResponse>>> getAllVideos(
            @RequestParam(name = "category", required = false) String category
    ) {
        List<YouTubeVideoResponse> videos = videoService.getAllVideos(category);
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    @Operation(summary = "Get all YouTube videos for admin", description = "Retrieves full list of YouTube videos for admin management. Requires admin JWT.")
    @SecurityRequirement(name = BEARER_AUTH)
    @GetMapping("/api/v1/admin/youtube/videos")
    public ResponseEntity<ApiResponse<List<YouTubeVideoResponse>>> getAllVideosAdmin() {
        List<YouTubeVideoResponse> videos = videoService.getAllVideos(null);
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    @Operation(summary = "Get YouTube video by ID")
    @GetMapping("/api/v1/youtube/videos/{id}")
    public ResponseEntity<ApiResponse<YouTubeVideoResponse>> getVideoById(@PathVariable Long id) {
        YouTubeVideoResponse video = videoService.getVideoById(id);
        return ResponseEntity.ok(ApiResponse.success(video));
    }

    @Operation(summary = "Create YouTube video", description = "Adds a YouTube video link. Requires admin JWT.")
    @SecurityRequirement(name = BEARER_AUTH)
    @PostMapping("/api/v1/admin/youtube/videos")
    public ResponseEntity<ApiResponse<YouTubeVideoResponse>> createVideo(
            @Valid @RequestBody YouTubeVideoCreateRequest request
    ) {
        YouTubeVideoResponse video = videoService.createVideo(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("YouTube video created successfully", video));
    }

    @Operation(summary = "Update YouTube video", description = "Updates video details. Requires admin JWT.")
    @SecurityRequirement(name = BEARER_AUTH)
    @PutMapping("/api/v1/admin/youtube/videos/{id}")
    public ResponseEntity<ApiResponse<YouTubeVideoResponse>> updateVideo(
            @PathVariable Long id,
            @Valid @RequestBody YouTubeVideoUpdateRequest request
    ) {
        YouTubeVideoResponse video = videoService.updateVideo(id, request);
        return ResponseEntity.ok(ApiResponse.success("YouTube video updated successfully", video));
    }

    @Operation(summary = "Delete YouTube video", description = "Deletes a video. Requires admin JWT.")
    @SecurityRequirement(name = BEARER_AUTH)
    @DeleteMapping("/api/v1/admin/youtube/videos/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok(ApiResponse.success("YouTube video deleted successfully", null));
    }
}
