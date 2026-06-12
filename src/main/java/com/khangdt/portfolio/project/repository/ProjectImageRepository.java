package com.khangdt.portfolio.project.repository;

import com.khangdt.portfolio.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    List<ProjectImage> findByProjectIdOrderByDisplayOrderAsc(Long projectId);

    void deleteByProjectId(Long projectId);
}
