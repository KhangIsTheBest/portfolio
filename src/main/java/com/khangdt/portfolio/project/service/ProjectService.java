package com.khangdt.portfolio.project.service;

import com.khangdt.portfolio.project.dto.request.ProjectCreateRequest;
import com.khangdt.portfolio.project.dto.request.ProjectUpdateRequest;
import com.khangdt.portfolio.project.dto.response.ProjectResponse;
import com.khangdt.portfolio.project.dto.response.ProjectSummaryResponse;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectResponse createProject(ProjectCreateRequest request, Long createdById);

    ProjectResponse updateProject(Long id, ProjectUpdateRequest request);

    void deleteProject(Long id);

    ProjectResponse getProjectById(Long id);

    ProjectResponse getProjectBySlug(String slug);

    Page<ProjectSummaryResponse> getProjects(ProjectStatus status, Pageable pageable);

    Page<ProjectSummaryResponse> getFeaturedProjects(Pageable pageable);
}
