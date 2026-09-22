package com.khangdt.portfolio.youtube.service;

import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoCreateRequest;
import com.khangdt.portfolio.youtube.dto.request.YouTubeVideoUpdateRequest;
import com.khangdt.portfolio.youtube.dto.response.YouTubeVideoResponse;

import java.util.List;

public interface YouTubeVideoService {

    List<YouTubeVideoResponse> getAllVideos(String category);

    YouTubeVideoResponse getVideoById(Long id);

    YouTubeVideoResponse createVideo(YouTubeVideoCreateRequest request);

    YouTubeVideoResponse updateVideo(Long id, YouTubeVideoUpdateRequest request);

    void deleteVideo(Long id);
}
