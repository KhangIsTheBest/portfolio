package com.khangdt.portfolio.technology.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.common.response.PagedResponse;
import com.khangdt.portfolio.technology.dto.request.TechnologyCreateRequest;
import com.khangdt.portfolio.technology.dto.request.TechnologyUpdateRequest;
import com.khangdt.portfolio.technology.dto.response.TechnologyResponse;
import com.khangdt.portfolio.technology.service.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Technologies", description = "Technology management APIs")
public class TechnologyController {

    private static final String BEARER_AUTH = "Bearer Authentication";

    private final TechnologyService technologyService;

    @Operation(
            summary = "Get technologies",
            description = "Returns a paginated list of all technologies"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Technologies retrieved successfully"
            )
    })
    @GetMapping("/api/v1/technologies")
    public ResponseEntity<ApiResponse<PagedResponse<TechnologyResponse>>> getTechnologies(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<TechnologyResponse> technologies = technologyService.getTechnologies(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(technologies)));
    }

    @Operation(
            summary = "Create technology",
            description = "Creates a new technology. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Technology created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Technology name already exists"
            )
    })
    @PostMapping("/api/v1/admin/technologies")
    public ResponseEntity<ApiResponse<TechnologyResponse>> createTechnology(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Technology creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TechnologyCreateRequest.class))
            )
            @Valid @RequestBody TechnologyCreateRequest request
    ) {
        TechnologyResponse technology = technologyService.createTechnology(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Technology created successfully", technology));
    }

    @Operation(
            summary = "Update technology",
            description = "Updates an existing technology. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Technology updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Technology not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Technology name already exists"
            )
    })
    @PutMapping("/api/v1/admin/technologies/{id}")
    public ResponseEntity<ApiResponse<TechnologyResponse>> updateTechnology(
            @Parameter(description = "Technology ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TechnologyUpdateRequest request
    ) {
        TechnologyResponse technology = technologyService.updateTechnology(id, request);
        return ResponseEntity.ok(ApiResponse.success("Technology updated successfully", technology));
    }

    @Operation(
            summary = "Delete technology",
            description = "Deletes a technology and removes it from linked projects. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Technology deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Technology not found"
            )
    })
    @DeleteMapping("/api/v1/admin/technologies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTechnology(
            @Parameter(description = "Technology ID", example = "1")
            @PathVariable Long id
    ) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.ok(ApiResponse.success("Technology deleted successfully", null));
    }
}
