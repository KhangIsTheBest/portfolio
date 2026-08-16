package com.khangdt.portfolio.project.repository;

import com.khangdt.portfolio.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    List<ProjectImage> findByProjectIdOrderByDisplayOrderAsc(Long projectId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ProjectImage pi WHERE pi.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
