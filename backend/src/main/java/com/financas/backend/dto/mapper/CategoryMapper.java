package com.financas.backend.dto.mapper;

import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;

public class CategoryMapper {

    public static CategoryResponseDTO mapCategoryToCategoryResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getCategoryType()
        );
    }

    public static Category mapCategoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();

        category.setName(categoryRequestDTO.getName());
        category.setCategoryType(categoryRequestDTO.getCategoryType());

        return category;
    }
}
