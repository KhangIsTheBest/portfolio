package com.khangdt.portfolio.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String thumbnailUrl;
    private Boolean published;
    private BlogAuthorResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
