package com.khangdt.portfolio.project.dto.response;

import com.khangdt.portfolio.project.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSummaryResponse {

    private Long id;
    private String title;
    private String slug;
    private String shortDescription;
    private String thumbnailUrl;
    private Boolean featured;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<ProjectTechnologyResponse> technologies = new ArrayList<>();
}
