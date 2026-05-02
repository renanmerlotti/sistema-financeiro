package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.CategoryMapper;
import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.UserRepository;
import com.financas.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO, Long userId) {
        Category category = CategoryMapper.mapCategoryRequestDTOToCategory(categoryRequestDTO);
        category.setUser(userRepository.getReferenceById(userId));
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.mapCategoryToCategoryResponseDTO(savedCategory);
    }

    @Override
    public List<CategoryResponseDTO> listCategoriesByUserId(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map((category) -> CategoryMapper.mapCategoryToCategoryResponseDTO(category))
                .collect(Collectors.toList());
    }
}
