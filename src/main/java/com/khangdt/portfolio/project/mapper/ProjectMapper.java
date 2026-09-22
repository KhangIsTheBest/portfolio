package com.khangdt.portfolio.project.mapper;

import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.project.dto.request.ProjectCreateRequest;
import com.khangdt.portfolio.project.dto.request.ProjectImageRequest;
import com.khangdt.portfolio.project.dto.request.ProjectUpdateRequest;
import com.khangdt.portfolio.project.dto.response.ProjectAuthorResponse;
import com.khangdt.portfolio.project.dto.response.ProjectImageResponse;
import com.khangdt.portfolio.project.dto.response.ProjectResponse;
import com.khangdt.portfolio.project.dto.response.ProjectSummaryResponse;
import com.khangdt.portfolio.project.dto.response.ProjectTechnologyResponse;
import com.khangdt.portfolio.project.entity.Project;
import com.khangdt.portfolio.project.entity.ProjectImage;
import com.khangdt.portfolio.technology.entity.Technology;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectCreateRequest request) {
        return Project.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .content(request.getContent())
                .githubUrl(normalizeUrl(request.getGithubUrl()))
                .demoUrl(normalizeUrl(request.getDemoUrl()))
                .thumbnailUrl(normalizeUrl(request.getThumbnailUrl()))
                .featured(request.getFeatured())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isCurrent(request.getIsCurrent() != null ? request.getIsCurrent() : false)
                .contentType(request.getContentType() != null ? request.getContentType() : "MARKDOWN")
                .status(request.getStatus())
                .build();
    }

    public void updateEntity(Project project, ProjectUpdateRequest request) {
        project.setTitle(request.getTitle());
        project.setSlug(request.getSlug());
        project.setShortDescription(request.getShortDescription());
        project.setContent(request.getContent());
        project.setGithubUrl(normalizeUrl(request.getGithubUrl()));
        project.setDemoUrl(normalizeUrl(request.getDemoUrl()));
        project.setThumbnailUrl(normalizeUrl(request.getThumbnailUrl()));
        project.setFeatured(request.getFeatured());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());
        if (request.getIsCurrent() != null) project.setIsCurrent(request.getIsCurrent());
        if (request.getContentType() != null) project.setContentType(request.getContentType());
        project.setStatus(request.getStatus());
    }

    public ProjectImage toProjectImage(ProjectImageRequest request) {
        return ProjectImage.builder()
                .imageUrl(request.getImageUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
    }

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .shortDescription(project.getShortDescription())
                .content(project.getContent())
                .githubUrl(project.getGithubUrl())
                .demoUrl(project.getDemoUrl())
                .thumbnailUrl(project.getThumbnailUrl())
                .featured(project.getFeatured())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isCurrent(project.getIsCurrent())
                .contentType(project.getContentType())
                .status(project.getStatus())
                .createdBy(toAuthorResponse(project.getCreatedBy()))
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .images(toImageResponseList(project.getImages()))
                .technologies(toTechnologyResponseList(project.getTechnologies()))
                .build();
    }

    public ProjectSummaryResponse toSummaryResponse(Project project) {
        if (project == null) {
            return null;
        }

        return ProjectSummaryResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .shortDescription(project.getShortDescription())
                .thumbnailUrl(project.getThumbnailUrl())
                .featured(project.getFeatured())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isCurrent(project.getIsCurrent())
                .contentType(project.getContentType())
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .technologies(toTechnologyResponseList(project.getTechnologies()))
                .build();
    }

    public List<ProjectSummaryResponse> toSummaryResponseList(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return Collections.emptyList();
        }

        return projects.stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    public ProjectImageResponse toImageResponse(ProjectImage image) {
        if (image == null) {
            return null;
        }

        return ProjectImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .displayOrder(image.getDisplayOrder())
                .createdAt(image.getCreatedAt())
                .build();
    }

    public List<ProjectImageResponse> toImageResponseList(List<ProjectImage> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        java.util.Set<Object> seenKeys = new java.util.HashSet<>();
        java.util.List<ProjectImageResponse> result = new java.util.ArrayList<>();

        for (ProjectImage image : images) {
            if (image == null) {
                continue;
            }
            Object key = image.getId() != null ? image.getId() : image.getImageUrl();
            if (key != null && seenKeys.add(key)) {
                result.add(toImageResponse(image));
            }
        }

        return result;
    }

    public ProjectTechnologyResponse toTechnologyResponse(Technology technology) {
        if (technology == null) {
            return null;
        }

        return ProjectTechnologyResponse.builder()
                .id(technology.getId())
                .name(technology.getName())
                .iconUrl(technology.getIconUrl())
                .build();
    }

    public List<ProjectTechnologyResponse> toTechnologyResponseList(Iterable<Technology> technologies) {
        if (technologies == null) {
            return Collections.emptyList();
        }

        return java.util.stream.StreamSupport.stream(technologies.spliterator(), false)
                .map(this::toTechnologyResponse)
                .toList();
    }

    public ProjectAuthorResponse toAuthorResponse(User user) {
        if (user == null) {
            return null;
        }

        return ProjectAuthorResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        return url.trim();
    }
}
