package com.khangdt.portfolio.youtube.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeVideoUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 500, message = "Video URL must not exceed 500 characters")
    private String videoUrlOrId;

    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    private String thumbnailUrl;

    @Size(max = 20, message = "Duration must not exceed 20 characters")
    private String duration;

    private Boolean featured;

    private Integer displayOrder;
}
