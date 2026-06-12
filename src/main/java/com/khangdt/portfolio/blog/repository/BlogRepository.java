package com.khangdt.portfolio.blog.repository;

import com.khangdt.portfolio.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @EntityGraph(attributePaths = {"author"})
    Optional<Blog> findBySlug(String slug);

    @EntityGraph(attributePaths = {"author"})
    Optional<Blog> findDetailedById(Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @EntityGraph(attributePaths = {"author"})
    Page<Blog> findByPublished(boolean published, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author"})
    Page<Blog> findAll(Pageable pageable);
}
