package com.khangdt.portfolio.blog.service.impl;

import com.khangdt.portfolio.auth.security.SecurityUtils;
import com.khangdt.portfolio.blog.dto.request.BlogCreateRequest;
import com.khangdt.portfolio.blog.dto.request.BlogUpdateRequest;
import com.khangdt.portfolio.blog.dto.response.BlogResponse;
import com.khangdt.portfolio.blog.entity.Blog;
import com.khangdt.portfolio.blog.mapper.BlogMapper;
import com.khangdt.portfolio.blog.repository.BlogRepository;
import com.khangdt.portfolio.blog.service.BlogService;
import com.khangdt.portfolio.common.exception.DuplicateResourceException;
import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogServiceImpl implements BlogService {

    private static final String RESOURCE_NAME = "Blog";

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public Page<BlogResponse> getBlogs(Boolean publishedOnly, Pageable pageable) {
        if (Boolean.TRUE.equals(publishedOnly)) {
            return blogRepository.findByPublished(true, pageable)
                    .map(blogMapper::toResponse);
        }
        return blogRepository.findAll(pageable)
                .map(blogMapper::toResponse);
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        return blogMapper.toResponse(blog);
    }

    @Override
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "slug", slug));
        
        // Chỉ cho phép xem công khai nếu bài viết đã được xuất bản
        if (Boolean.FALSE.equals(blog.getPublished())) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "slug", slug);
        }
        return blogMapper.toResponse(blog);
    }

    @Override
    @Transactional
    public BlogResponse createBlog(BlogCreateRequest request) {
        if (blogRepository.existsBySlug(request.getSlug().trim())) {
            throw new DuplicateResourceException(RESOURCE_NAME, "slug", request.getSlug());
        }

        Blog blog = blogMapper.toEntity(request);
        blog.setAuthor(SecurityUtils.getCurrentUser());

        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toResponse(savedBlog);
    }

    @Override
    @Transactional
    public BlogResponse updateBlog(Long id, BlogUpdateRequest request) {
        Blog blog = findBlogById(id);

        if (blogRepository.existsBySlugAndIdNot(request.getSlug().trim(), id)) {
            throw new DuplicateResourceException(RESOURCE_NAME, "slug", request.getSlug());
        }

        blogMapper.updateEntity(blog, request);
        Blog updatedBlog = blogRepository.save(blog);
        return blogMapper.toResponse(updatedBlog);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        Blog blog = findBlogById(id);
        blogRepository.delete(blog);
    }

    private Blog findBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }
}
