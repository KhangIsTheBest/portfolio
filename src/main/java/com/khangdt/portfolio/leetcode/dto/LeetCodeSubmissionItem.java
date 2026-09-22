package com.khangdt.portfolio.leetcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeetCodeSubmissionItem implements Serializable {

    private String id;
    private String title;
    private String titleSlug;
    private String difficulty;
    private String statusDisplay;
    private String lang;
    private String runtime;
    private String memory;
    private Long timestamp;
    private String code;
}
