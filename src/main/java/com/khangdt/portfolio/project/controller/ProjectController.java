package com.khangdt.portfolio.project.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.common.response.PagedResponse;
import com.khangdt.portfolio.project.dto.request.ProjectCreateRequest;
import com.khangdt.portfolio.project.dto.request.ProjectUpdateRequest;
import com.khangdt.portfolio.project.dto.response.ProjectResponse;
import com.khangdt.portfolio.project.dto.response.ProjectSummaryResponse;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import com.khangdt.portfolio.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management APIs")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Get projects",
            description = "Returns a paginated list of projects filtered by status"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Projects retrieved successfully"
            )
    })
    @GetMapping("/api/v1/projects")
    public ResponseEntity<ApiResponse<PagedResponse<ProjectSummaryResponse>>> getProjects(
            @Parameter(description = "Project status filter", example = "PUBLISHED")
            @RequestParam(defaultValue = "PUBLISHED") ProjectStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProjectSummaryResponse> projects = projectService.getProjects(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(projects)));
    }

    @Operation(
            summary = "Get featured projects",
            description = "Returns published projects marked as featured"
    )
    @GetMapping("/api/v1/projects/featured")
    public ResponseEntity<ApiResponse<PagedResponse<ProjectSummaryResponse>>> getFeaturedProjects(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProjectSummaryResponse> projects = projectService.getFeaturedProjects(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(projects)));
    }

    @Operation(
            summary = "Get project by slug",
            description = "Returns full project details using SEO-friendly slug"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Project found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @GetMapping("/api/v1/projects/slug/{slug}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectBySlug(
            @Parameter(description = "Project slug", example = "portfolio-website")
            @PathVariable String slug
    ) {
        ProjectResponse project = projectService.getProjectBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @Operation(
            summary = "Get project by ID",
            description = "Returns full project details by numeric ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Project found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @GetMapping("/api/v1/projects/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id
    ) {
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @Operation(
            summary = "Create project",
            description = "Creates a new project. Admin endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Project created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error or invalid technology IDs"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Slug already exists"
            )
    })
    @PostMapping("/api/v1/admin/projects")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Project creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProjectCreateRequest.class))
            )
            @Valid @RequestBody ProjectCreateRequest request,
            @Parameter(description = "Creator user ID (temporary until auth module is ready)", example = "1")
            @RequestParam(required = false) Long createdById
    ) {
        ProjectResponse project = projectService.createProject(request, createdById);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", project));
    }

    @Operation(
            summary = "Update project",
            description = "Updates an existing project. Admin endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Project updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Slug already exists"
            )
    })
    @PutMapping("/api/v1/admin/projects/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest request
    ) {
        ProjectResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", project));
    }

    @Operation(
            summary = "Delete project",
            description = "Deletes a project and its related images. Admin endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Project deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @DeleteMapping("/api/v1/admin/projects/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", null));
    }
}
