package com.khangdt.portfolio.technology.dto.response;

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
public class TechnologyResponse {

    private Long id;
    private String name;
    private String iconUrl;
    private LocalDateTime createdAt;
}
