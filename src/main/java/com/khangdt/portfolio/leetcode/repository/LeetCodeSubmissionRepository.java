package com.khangdt.portfolio.leetcode.repository;

import com.khangdt.portfolio.leetcode.entity.LeetCodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeetCodeSubmissionRepository extends JpaRepository<LeetCodeSubmission, String> {

    List<LeetCodeSubmission> findAllByOrderByTimestampDesc();

    List<LeetCodeSubmission> findByDifficultyOrderByTimestampDesc(String difficulty);

    Optional<LeetCodeSubmission> findByTitleSlug(String titleSlug);

    boolean existsById(String id);
}
