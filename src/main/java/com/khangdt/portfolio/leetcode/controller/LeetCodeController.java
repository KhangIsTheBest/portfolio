package com.khangdt.portfolio.leetcode.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.leetcode.dto.LeetCodeStatsResponse;
import com.khangdt.portfolio.leetcode.dto.LeetCodeSubmissionItem;
import com.khangdt.portfolio.leetcode.service.LeetCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "LeetCode", description = "LeetCode integration and statistics APIs")
public class LeetCodeController {

    private final LeetCodeService leetCodeService;

    @Operation(summary = "Get LeetCode public statistics", description = "Fetches solved counts, rankings, contest rating, and calendar")
    @GetMapping("/api/v1/leetcode/stats")
    public ResponseEntity<ApiResponse<LeetCodeStatsResponse>> getStats() {
        LeetCodeStatsResponse response = leetCodeService.getStats();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get LeetCode recent submissions", description = "Fetches list of recent accepted problem submissions")
    @GetMapping("/api/v1/leetcode/submissions")
    public ResponseEntity<ApiResponse<List<LeetCodeSubmissionItem>>> getSubmissions(
            @RequestParam(name = "limit", defaultValue = "20") int limit
    ) {
        List<LeetCodeSubmissionItem> response = leetCodeService.getSubmissions(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get LeetCode submission source code", description = "Fetches syntax code for a submission ID")
    @GetMapping("/api/v1/leetcode/submissions/{id}/code")
    public ResponseEntity<ApiResponse<LeetCodeSubmissionItem>> getSubmissionCode(
            @PathVariable String id
    ) {
        LeetCodeSubmissionItem response = leetCodeService.getSubmissionCode(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Evict LeetCode cache and force sync", description = "Admin cache refresh trigger")
    @PostMapping("/api/v1/admin/leetcode/sync")
    public ResponseEntity<ApiResponse<String>> syncLeetCode() {
        leetCodeService.evictCache();
        return ResponseEntity.ok(ApiResponse.success("LeetCode cache evicted. Next request will sync fresh stats from LeetCode."));
    }
}
