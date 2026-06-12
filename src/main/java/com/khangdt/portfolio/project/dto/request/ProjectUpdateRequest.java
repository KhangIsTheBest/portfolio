package com.khangdt.portfolio.project.dto.request;

import com.khangdt.portfolio.project.entity.ProjectStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 200, message = "Slug must not exceed 200 characters")
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    private String content;

    @Size(max = 500, message = "GitHub URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "GitHub URL must be empty or a valid http(s) URL"
    )
    private String githubUrl;

    @Size(max = 500, message = "Demo URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "Demo URL must be empty or a valid http(s) URL"
    )
    private String demoUrl;

    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://\\S+)$",
            message = "Thumbnail URL must be empty or a valid http(s) URL"
    )
    private String thumbnailUrl;

    @NotNull(message = "Featured flag is required")
    private Boolean featured;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    @Builder.Default
    private List<Long> technologyIds = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<ProjectImageRequest> images = new ArrayList<>();
}
