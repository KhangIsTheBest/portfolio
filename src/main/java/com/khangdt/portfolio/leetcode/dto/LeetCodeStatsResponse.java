package com.khangdt.portfolio.leetcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeetCodeStatsResponse implements Serializable {

    private String username;
    private String realName;
    private String avatar;
    private Integer ranking;

    private Integer totalSolved;
    private Integer easySolved;
    private Integer mediumSolved;
    private Integer hardSolved;

    private Integer totalQuestions;
    private Integer easyQuestions;
    private Integer mediumQuestions;
    private Integer hardQuestions;

    private Double acceptanceRate;

    private Double contestRating;
    private Integer contestGlobalRanking;
    private Double contestTopPercentage;
    private Integer totalContests;
    private String contestBadge;

    private Map<String, Integer> submissionCalendar;
}
