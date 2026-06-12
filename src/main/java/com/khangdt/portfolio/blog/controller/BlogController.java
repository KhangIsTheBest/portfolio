package com.khangdt.portfolio.blog.controller;

import com.khangdt.portfolio.blog.dto.request.BlogCreateRequest;
import com.khangdt.portfolio.blog.dto.request.BlogUpdateRequest;
import com.khangdt.portfolio.blog.dto.response.BlogResponse;
import com.khangdt.portfolio.blog.service.BlogService;
import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.common.response.PagedResponse;
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
@Tag(name = "Blogs", description = "Blog posts and articles APIs")
public class BlogController {

    private static final String BEARER_AUTH = "Bearer Authentication";

    private final BlogService blogService;

    @Operation(
            summary = "Get published blogs",
            description = "Returns a paginated list of published blog posts (Public)."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Published blogs retrieved successfully"
            )
    })
    @GetMapping("/api/v1/blogs")
    public ResponseEntity<ApiResponse<PagedResponse<BlogResponse>>> getPublishedBlogs(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BlogResponse> blogs = blogService.getBlogs(true, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(blogs)));
    }

    @Operation(
            summary = "Get published blog by slug",
            description = "Returns full details of a published blog post using its SEO-friendly slug (Public)."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Blog post found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Blog post not found"
            )
    })
    @GetMapping("/api/v1/blogs/slug/{slug}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogBySlug(
            @Parameter(description = "Blog slug", example = "my-first-blog")
            @PathVariable String slug
    ) {
        BlogResponse response = blogService.getBlogBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Get published blog by ID",
            description = "Returns details of a published blog post by numeric ID (Public)."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Blog post found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Blog post not found"
            )
    })
    @GetMapping("/api/v1/blogs/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogById(
            @Parameter(description = "Blog ID", example = "1")
            @PathVariable Long id
    ) {
        BlogResponse response = blogService.getBlogById(id);
        if (Boolean.FALSE.equals(response.getPublished())) {
            throw new ResourceNotFoundException("Blog", "id", id);
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Get all blogs (Admin)",
            description = "Returns a paginated list of all blog posts including drafts. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "All blogs retrieved successfully"
            )
    })
    @GetMapping("/api/v1/admin/blogs")
    public ResponseEntity<ApiResponse<PagedResponse<BlogResponse>>> getAllBlogs(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BlogResponse> blogs = blogService.getBlogs(false, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(blogs)));
    }

    @Operation(
            summary = "Get blog details by ID (Admin)",
            description = "Returns full details of any blog post (including drafts) by ID. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Blog post found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Blog post not found"
            )
    })
    @GetMapping("/api/v1/admin/blogs/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogByIdAdmin(
            @Parameter(description = "Blog ID", example = "1")
            @PathVariable Long id
    ) {
        BlogResponse response = blogService.getBlogById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Create blog post",
            description = "Creates a new blog post and sets the logged-in admin as the author. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Blog post created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation errors"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Slug already exists"
            )
    })
    @PostMapping("/api/v1/admin/blogs")
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Blog creation payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BlogCreateRequest.class))
            )
            @Valid @RequestBody BlogCreateRequest request
    ) {
        BlogResponse response = blogService.createBlog(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Blog post created successfully", response));
    }

    @Operation(
            summary = "Update blog post",
            description = "Updates an existing blog post. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Blog post updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Blog post not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Slug already exists"
            )
    })
    @PutMapping("/api/v1/admin/blogs/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @Parameter(description = "Blog ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BlogUpdateRequest request
    ) {
        BlogResponse response = blogService.updateBlog(id, request);
        return ResponseEntity.ok(ApiResponse.success("Blog post updated successfully", response));
    }

    @Operation(
            summary = "Delete blog post",
            description = "Deletes a blog post by ID. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Blog post deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Blog post not found"
            )
    })
    @DeleteMapping("/api/v1/admin/blogs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(
            @Parameter(description = "Blog ID", example = "1")
            @PathVariable Long id
    ) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(ApiResponse.success("Blog post deleted successfully", null));
    }
}
