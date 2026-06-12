package com.khangdt.portfolio.blog.mapper;

import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.blog.dto.request.BlogCreateRequest;
import com.khangdt.portfolio.blog.dto.request.BlogUpdateRequest;
import com.khangdt.portfolio.blog.dto.response.BlogAuthorResponse;
import com.khangdt.portfolio.blog.dto.response.BlogResponse;
import com.khangdt.portfolio.blog.entity.Blog;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public Blog toEntity(BlogCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Blog.builder()
                .title(normalizeText(request.getTitle()))
                .slug(normalizeText(request.getSlug()))
                .summary(normalizeText(request.getSummary()))
                .content(request.getContent())
                .thumbnailUrl(normalizeUrl(request.getThumbnailUrl()))
                .published(request.getPublished())
                .build();
    }

    public void updateEntity(Blog blog, BlogUpdateRequest request) {
        if (blog == null || request == null) {
            return;
        }

        blog.setTitle(normalizeText(request.getTitle()));
        blog.setSlug(normalizeText(request.getSlug()));
        blog.setSummary(normalizeText(request.getSummary()));
        blog.setContent(request.getContent());
        blog.setThumbnailUrl(normalizeUrl(request.getThumbnailUrl()));
        blog.setPublished(request.getPublished());
    }

    public BlogResponse toResponse(Blog blog) {
        if (blog == null) {
            return null;
        }

        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .summary(blog.getSummary())
                .content(blog.getContent())
                .thumbnailUrl(blog.getThumbnailUrl())
                .published(blog.getPublished())
                .author(toAuthorResponse(blog.getAuthor()))
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    public BlogAuthorResponse toAuthorResponse(User user) {
        if (user == null) {
            return null;
        }

        return BlogAuthorResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        return url.trim();
    }
}
