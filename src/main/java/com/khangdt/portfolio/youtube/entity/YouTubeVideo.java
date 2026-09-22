package com.khangdt.portfolio.youtube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "youtube_videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class YouTubeVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "video_id", nullable = false, length = 50)
    private String videoId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    @Builder.Default
    private String category = "General";

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(length = 20)
    private String duration;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
