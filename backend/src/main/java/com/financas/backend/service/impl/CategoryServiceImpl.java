package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.CategoryMapper;
import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;
import com.financas.backend.exception.BusinessRuleException;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.TransactionRepository;
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
    private final TransactionRepository transactionRepository;

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

    @Override
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO dto, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));

        category.setName(dto.getName());
        category.setCategoryType(dto.getCategoryType());

        Category updated = categoryRepository.save(category);

        return CategoryMapper.mapCategoryToCategoryResponseDTO(updated);

    }

    @Override
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category with id " + categoryId + " not found");
        }

        if (transactionRepository.existsByCategoryId(categoryId)) {
            throw new BusinessRuleException("Cannot delete category with associated transactions");
        }

        categoryRepository.delete(category);
    }
}
