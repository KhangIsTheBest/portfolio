package com.khangdt.portfolio.project.service;

import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.project.dto.request.ProjectCreateRequest;
import com.khangdt.portfolio.project.dto.response.ProjectResponse;
import com.khangdt.portfolio.project.dto.response.ProjectSummaryResponse;
import com.khangdt.portfolio.project.entity.Project;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import com.khangdt.portfolio.project.mapper.ProjectMapper;
import com.khangdt.portfolio.project.repository.ProjectRepository;
import com.khangdt.portfolio.project.service.impl.ProjectServiceImpl;
import com.khangdt.portfolio.technology.repository.TechnologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void getProjectBySlug_Success() {
        Project project = Project.builder()
                .id(1L)
                .title("Portfolio Website")
                .slug("portfolio-website")
                .status(ProjectStatus.PUBLISHED)
                .build();

        ProjectResponse response = ProjectResponse.builder()
                .id(1L)
                .title("Portfolio Website")
                .slug("portfolio-website")
                .build();

        when(projectRepository.findBySlug("portfolio-website")).thenReturn(Optional.of(project));
        when(projectMapper.toResponse(project)).thenReturn(response);

        ProjectResponse result = projectService.getProjectBySlug("portfolio-website");

        assertNotNull(result);
        assertEquals("portfolio-website", result.getSlug());
    }

    @Test
    void getProjectBySlug_NotFound_ThrowsException() {
        when(projectRepository.findBySlug("non-existent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectBySlug("non-existent"));
    }

    @Test
    void getProjects_FiltersByPublishedStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Project project = Project.builder().id(1L).title("Sample").status(ProjectStatus.PUBLISHED).build();
        Page<Project> page = new PageImpl<>(Collections.singletonList(project));

        when(projectRepository.findByStatus(ProjectStatus.PUBLISHED, pageable)).thenReturn(page);
        when(projectMapper.toSummaryResponse(any())).thenReturn(ProjectSummaryResponse.builder().id(1L).title("Sample").build());

        Page<ProjectSummaryResponse> result = projectService.getProjects(ProjectStatus.PUBLISHED, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(projectRepository, times(1)).findByStatus(ProjectStatus.PUBLISHED, pageable);
    }
}
