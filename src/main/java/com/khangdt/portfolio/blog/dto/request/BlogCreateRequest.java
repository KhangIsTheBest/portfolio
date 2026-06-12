package com.khangdt.portfolio.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;

    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

    @NotBlank(message = "Content is required")
    private String content;

    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "Thumbnail URL must be empty or a valid http(s) URL"
    )
    private String thumbnailUrl;

    @NotNull(message = "Published flag is required")
    @Builder.Default
    private Boolean published = true;
}
