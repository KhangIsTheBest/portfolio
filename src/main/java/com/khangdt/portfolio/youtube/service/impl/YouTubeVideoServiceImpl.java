package com.khangdt.portfolio.youtube.service.impl;

import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoCreateRequest;
import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoUpdateRequest;
import com.khangdt.portfolio.youtube.dto.response.YouTubeVideoResponse;
import com.khangdt.portfolio.youtube.entity.YouTubeVideo;
import com.khangdt.portfolio.youtube.repository.YouTubeVideoRepository;
import com.khangdt.portfolio.youtube.service.YouTubeVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class YouTubeVideoServiceImpl implements YouTubeVideoService {

    private final YouTubeVideoRepository videoRepository;

    private static final Pattern YOUTUBE_PATTERN = Pattern.compile(
            "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:watch\\?v=|embed\\/|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
    );

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "youtube_videos", key = "#category != null ? #category : 'all'", unless = "#result == null")
    public List<YouTubeVideoResponse> getAllVideos(String category) {
        List<YouTubeVideo> videos;
        if (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) {
            videos = videoRepository.findByCategoryOrderByDisplayOrderAscCreatedAtDesc(category);
        } else {
            videos = videoRepository.findAllByOrderByDisplayOrderAscCreatedAtDesc();
        }
        return videos.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public YouTubeVideoResponse getVideoById(Long id) {
        YouTubeVideo video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("YouTube video not found with id: " + id));
        return toResponse(video);
    }

    @Override
    @Transactional
    @CacheEvict(value = "youtube_videos", allEntries = true)
    public YouTubeVideoResponse createVideo(YouTubeVideoCreateRequest request) {
        String videoId = extractYouTubeVideoId(request.getVideoUrlOrId());
        String thumbnailUrl = request.getThumbnailUrl();
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
        }

        YouTubeVideo video = YouTubeVideo.builder()
                .title(request.getTitle())
                .videoId(videoId)
                .description(request.getDescription())
                .category(request.getCategory() != null && !request.getCategory().isBlank() ? request.getCategory() : "Java & Spring Boot")
                .thumbnailUrl(thumbnailUrl)
                .duration(request.getDuration() != null ? request.getDuration() : "10:00")
                .publishedAt(LocalDateTime.now())
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        YouTubeVideo saved = videoRepository.save(video);
        log.info("Created YouTube video id={}, videoId={}", saved.getId(), saved.getVideoId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "youtube_videos", allEntries = true)
    public YouTubeVideoResponse updateVideo(Long id, YouTubeVideoUpdateRequest request) {
        YouTubeVideo video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("YouTube video not found with id: " + id));

        video.setTitle(request.getTitle());
        if (request.getVideoUrlOrId() != null && !request.getVideoUrlOrId().isBlank()) {
            video.setVideoId(extractYouTubeVideoId(request.getVideoUrlOrId()));
        }
        video.setDescription(request.getDescription());
        if (request.getCategory() != null) video.setCategory(request.getCategory());
        if (request.getThumbnailUrl() != null && !request.getThumbnailUrl().isBlank()) {
            video.setThumbnailUrl(request.getThumbnailUrl());
        }
        if (request.getDuration() != null) video.setDuration(request.getDuration());
        if (request.getFeatured() != null) video.setFeatured(request.getFeatured());
        if (request.getDisplayOrder() != null) video.setDisplayOrder(request.getDisplayOrder());

        YouTubeVideo updated = videoRepository.save(video);
        log.info("Updated YouTube video id={}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "youtube_videos", allEntries = true)
    public void deleteVideo(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new ResourceNotFoundException("YouTube video not found with id: " + id);
        }
        videoRepository.deleteById(id);
        log.info("Deleted YouTube video id={}", id);
    }

    private String extractYouTubeVideoId(String input) {
        if (input == null || input.isBlank()) {
            return "dQw4w9WgXcQ";
        }
        String trimmed = input.trim();
        if (trimmed.length() == 11 && !trimmed.contains("/") && !trimmed.contains("?")) {
            return trimmed;
        }
        Matcher matcher = YOUTUBE_PATTERN.matcher(trimmed);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return trimmed;
    }

    private YouTubeVideoResponse toResponse(YouTubeVideo entity) {
        if (entity == null) return null;
        String videoId = entity.getVideoId();
        String youtubeUrl = (videoId != null && !videoId.isBlank())
                ? "https://www.youtube.com/watch?v=" + videoId
                : "https://www.youtube.com";
        return YouTubeVideoResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .videoId(videoId)
                .youtubeUrl(youtubeUrl)
                .description(entity.getDescription())
                .category(entity.getCategory())
                .thumbnailUrl(entity.getThumbnailUrl())
                .duration(entity.getDuration())
                .publishedAt(entity.getPublishedAt())
                .featured(entity.getFeatured())
                .active(true)
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
