package com.khangdt.portfolio.blog.service;

import com.khangdt.portfolio.blog.dto.request.BlogCreateRequest;
import com.khangdt.portfolio.blog.dto.request.BlogUpdateRequest;
import com.khangdt.portfolio.blog.dto.response.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    Page<BlogResponse> getBlogs(Boolean publishedOnly, Pageable pageable);
    BlogResponse getBlogById(Long id);
    BlogResponse getBlogBySlug(String slug);
    BlogResponse createBlog(BlogCreateRequest request);
    BlogResponse updateBlog(Long id, BlogUpdateRequest request);
    void deleteBlog(Long id);
}
