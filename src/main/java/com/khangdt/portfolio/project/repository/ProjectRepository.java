package com.khangdt.portfolio.project.repository;

import com.khangdt.portfolio.project.entity.Project;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"images", "technologies", "createdBy"})
    Optional<Project> findBySlug(String slug);

    @EntityGraph(attributePaths = {"images", "technologies", "createdBy"})
    Optional<Project> findDetailedById(Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @EntityGraph(attributePaths = {"technologies"})
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"technologies"})
    Page<Project> findByFeaturedTrueAndStatus(ProjectStatus status, Pageable pageable);
}
