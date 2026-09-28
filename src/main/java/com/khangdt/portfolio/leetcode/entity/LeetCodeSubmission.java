package com.khangdt.portfolio.leetcode.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "leetcode_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LeetCodeSubmission {

    @Id
    @Column(length = 50, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "title_slug", nullable = false, length = 255)
    private String titleSlug;

    @Column(length = 20)
    private String difficulty;

    @Column(length = 50)
    private String lang;

    @Column(name = "status_display", length = 50)
    @Builder.Default
    private String statusDisplay = "Accepted";

    @Column(length = 50)
    private String runtime;

    @Column(length = 50)
    private String memory;

    @Column(columnDefinition = "TEXT")
    private String code;

    private Long timestamp;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
