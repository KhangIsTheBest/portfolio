package com.khangdt.portfolio.project.service.impl;

import com.khangdt.portfolio.auth.security.SecurityUtils;
import com.khangdt.portfolio.common.exception.BadRequestException;
import com.khangdt.portfolio.common.exception.DuplicateResourceException;
import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.project.dto.request.ProjectCreateRequest;
import com.khangdt.portfolio.project.dto.request.ProjectImageRequest;
import com.khangdt.portfolio.project.dto.request.ProjectUpdateRequest;
import com.khangdt.portfolio.project.dto.response.ProjectResponse;
import com.khangdt.portfolio.project.dto.response.ProjectSummaryResponse;
import com.khangdt.portfolio.project.entity.Project;
import com.khangdt.portfolio.project.entity.ProjectImage;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import com.khangdt.portfolio.project.mapper.ProjectMapper;
import com.khangdt.portfolio.project.repository.ProjectRepository;
import com.khangdt.portfolio.project.service.ProjectService;
import com.khangdt.portfolio.technology.entity.Technology;
import com.khangdt.portfolio.technology.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final String RESOURCE_NAME = "Project";

    private final ProjectRepository projectRepository;
    private final TechnologyRepository technologyRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        if (projectRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException(RESOURCE_NAME, "slug", request.getSlug());
        }

        Project project = projectMapper.toEntity(request);
        project.setCreatedBy(SecurityUtils.getCurrentUser());
        project.setTechnologies(resolveTechnologies(request.getTechnologyIds()));
        replaceImages(project, request.getImages());

        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponse(savedProject);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        Project project = findProjectById(id);

        if (projectRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new DuplicateResourceException(RESOURCE_NAME, "slug", request.getSlug());
        }

        projectMapper.updateEntity(project, request);
        project.setTechnologies(resolveTechnologies(request.getTechnologyIds()));
        replaceImages(project, request.getImages());

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toResponse(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = findProjectById(id);
        projectRepository.delete(project);
    }

    @Override
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse getProjectBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "slug", slug));
        return projectMapper.toResponse(project);
    }

    @Override
    public Page<ProjectSummaryResponse> getProjects(ProjectStatus status, Pageable pageable) {
        return projectRepository.findByStatus(status, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    @Override
    public Page<ProjectSummaryResponse> getFeaturedProjects(Pageable pageable) {
        return projectRepository.findByFeaturedTrueAndStatus(ProjectStatus.PUBLISHED, pageable)
                .map(projectMapper::toSummaryResponse);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    private Set<Technology> resolveTechnologies(List<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> uniqueIds = new HashSet<>(technologyIds);
        if (uniqueIds.size() != technologyIds.size()) {
            throw new BadRequestException("Duplicate technology IDs are not allowed");
        }

        List<Technology> technologies = technologyRepository.findAllById(uniqueIds);
        if (technologies.size() != uniqueIds.size()) {
            throw new BadRequestException("One or more technology IDs are invalid");
        }

        return new HashSet<>(technologies);
    }

    private void replaceImages(Project project, List<ProjectImageRequest> imageRequests) {
        project.getImages().clear();

        if (imageRequests == null || imageRequests.isEmpty()) {
            return;
        }

        for (ProjectImageRequest imageRequest : imageRequests) {
            ProjectImage image = projectMapper.toProjectImage(imageRequest);
            project.addImage(image);
        }
    }
}
