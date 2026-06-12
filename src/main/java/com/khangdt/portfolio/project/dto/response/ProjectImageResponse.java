package com.khangdt.portfolio.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectImageResponse {

    private Long id;
    private String imageUrl;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
