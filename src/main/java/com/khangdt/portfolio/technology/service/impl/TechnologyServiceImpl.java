package com.khangdt.portfolio.technology.service.impl;

import com.khangdt.portfolio.common.exception.DuplicateResourceException;
import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.technology.dto.request.TechnologyCreateRequest;
import com.khangdt.portfolio.technology.dto.request.TechnologyUpdateRequest;
import com.khangdt.portfolio.technology.dto.response.TechnologyResponse;
import com.khangdt.portfolio.technology.entity.Technology;
import com.khangdt.portfolio.technology.mapper.TechnologyMapper;
import com.khangdt.portfolio.technology.repository.TechnologyRepository;
import com.khangdt.portfolio.technology.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechnologyServiceImpl implements TechnologyService {

    private static final String RESOURCE_NAME = "Technology";

    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    @Override
    public Page<TechnologyResponse> getTechnologies(Pageable pageable) {
        return technologyRepository.findAll(pageable)
                .map(technologyMapper::toResponse);
    }

    @Override
    @Transactional
    public TechnologyResponse createTechnology(TechnologyCreateRequest request) {
        if (technologyRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new DuplicateResourceException(RESOURCE_NAME, "name", request.getName());
        }

        Technology technology = technologyMapper.toEntity(request);
        Technology savedTechnology = technologyRepository.save(technology);
        return technologyMapper.toResponse(savedTechnology);
    }

    @Override
    @Transactional
    public TechnologyResponse updateTechnology(Long id, TechnologyUpdateRequest request) {
        Technology technology = findTechnologyById(id);

        if (technologyRepository.existsByNameIgnoreCaseAndIdNot(request.getName().trim(), id)) {
            throw new DuplicateResourceException(RESOURCE_NAME, "name", request.getName());
        }

        technologyMapper.updateEntity(technology, request);
        Technology updatedTechnology = technologyRepository.save(technology);
        return technologyMapper.toResponse(updatedTechnology);
    }

    @Override
    @Transactional
    public void deleteTechnology(Long id) {
        Technology technology = findTechnologyById(id);
        technologyRepository.delete(technology);
    }

    private Technology findTechnologyById(Long id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }
}
