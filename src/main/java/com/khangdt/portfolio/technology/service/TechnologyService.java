package com.khangdt.portfolio.technology.service;

import com.khangdt.portfolio.technology.dto.request.TechnologyCreateRequest;
import com.khangdt.portfolio.technology.dto.request.TechnologyUpdateRequest;
import com.khangdt.portfolio.technology.dto.response.TechnologyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TechnologyService {

    Page<TechnologyResponse> getTechnologies(Pageable pageable);

    TechnologyResponse createTechnology(TechnologyCreateRequest request);

    TechnologyResponse updateTechnology(Long id, TechnologyUpdateRequest request);

    void deleteTechnology(Long id);
}
