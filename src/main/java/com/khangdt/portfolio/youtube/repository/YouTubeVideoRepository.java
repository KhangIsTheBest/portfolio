package com.khangdt.portfolio.youtube.repository;

import com.khangdt.portfolio.youtube.entity.YouTubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YouTubeVideoRepository extends JpaRepository<YouTubeVideo, Long> {

    List<YouTubeVideo> findAllByOrderByDisplayOrderAscCreatedAtDesc();

    List<YouTubeVideo> findByCategoryOrderByDisplayOrderAscCreatedAtDesc(String category);

    List<YouTubeVideo> findByFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();
}
