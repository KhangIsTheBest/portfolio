package com.khangdt.portfolio.leetcode.service;

import com.khangdt.portfolio.leetcode.dto.LeetCodeStatsResponse;
import com.khangdt.portfolio.leetcode.dto.LeetCodeSubmissionItem;

import java.util.List;

public interface LeetCodeService {

    LeetCodeStatsResponse getStats();

    List<LeetCodeSubmissionItem> getSubmissions(int limit);

    LeetCodeSubmissionItem getSubmissionCode(String submissionId);

    void evictCache();
}
