package com.financas.backend.service;

import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO, Long userId);

    List<CategoryResponseDTO> listCategoriesByUserId(Long userId);

    CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO dto, Long userId);

    void deleteCategory(Long categoryId, Long userId);
}