package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.CategoryMapper;
import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = CategoryMapper.mapCategoryRequestDTOToCategory(categoryRequestDTO);
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.mapCategoryToCategoryResponseDTO(savedCategory);
    }
}
