package com.khangdt.portfolio.youtube.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeVideoResponse implements Serializable {

    private Long id;
    private String title;
    private String videoId;
    private String youtubeUrl;
    private String description;
    private String category;
    private String thumbnailUrl;
    private String duration;
    private LocalDateTime publishedAt;
    private Boolean featured;
    private Boolean active;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
