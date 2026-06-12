package com.khangdt.portfolio.technology.mapper;

import com.khangdt.portfolio.technology.dto.request.TechnologyCreateRequest;
import com.khangdt.portfolio.technology.dto.request.TechnologyUpdateRequest;
import com.khangdt.portfolio.technology.dto.response.TechnologyResponse;
import com.khangdt.portfolio.technology.entity.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology toEntity(TechnologyCreateRequest request) {
        return Technology.builder()
                .name(request.getName().trim())
                .iconUrl(normalizeUrl(request.getIconUrl()))
                .build();
    }

    public void updateEntity(Technology technology, TechnologyUpdateRequest request) {
        technology.setName(request.getName().trim());
        technology.setIconUrl(normalizeUrl(request.getIconUrl()));
    }

    public TechnologyResponse toResponse(Technology technology) {
        if (technology == null) {
            return null;
        }

        return TechnologyResponse.builder()
                .id(technology.getId())
                .name(technology.getName())
                .iconUrl(technology.getIconUrl())
                .createdAt(technology.getCreatedAt())
                .build();
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        return url.trim();
    }
}
