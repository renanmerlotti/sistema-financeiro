package com.financas.backend.service.impl;

import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;
import com.financas.backend.entity.CategoryType;
import com.financas.backend.entity.User;
import com.financas.backend.exception.BusinessRuleException;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Should return CategoryResponseDTO when category is saved")
    void createCategory() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("Alimentacao", CategoryType.EXPENSE);
        Category category = new Category(1L, "Alimentacao", CategoryType.EXPENSE, user);

        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO, 1L);

        assertNotNull(categoryResponseDTO);
        assertEquals(1L, categoryResponseDTO.getId());
        assertEquals("Alimentacao", categoryResponseDTO.getName());
    }

    @Test
    @DisplayName("Should return list of CategoryResponseDTO when categories are found by user id")
    void listCategoriesByUserId() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Category category1 = new Category(1L, "Alimentacao", CategoryType.EXPENSE, user);
        Category category2 = new Category(2L, "Salario", CategoryType.INCOME, user);

        when(categoryRepository.findByUserId(1L)).thenReturn(List.of(category1, category2));

        List<CategoryResponseDTO> result = categoryService.listCategoriesByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alimentacao", result.getFirst().getName());
        assertEquals("Salario", result.get(1).getName());
    }

    @Test
    @DisplayName("Should return updated CategoryResponseDTO when category is found")
    void updateCategoryCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Category category = new Category(1L, "Alimentacao", CategoryType.EXPENSE, user);
        CategoryRequestDTO dto = new CategoryRequestDTO("Transporte", CategoryType.EXPENSE);
        Category updated = new Category(1L, "Transporte", CategoryType.EXPENSE, user);

        when(categoryRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        CategoryResponseDTO result = categoryService.updateCategory(1L, dto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Transporte", result.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category is not found on update")
    void updateCategoryCase2() {
        CategoryRequestDTO dto = new CategoryRequestDTO("Transporte", CategoryType.EXPENSE);

        when(categoryRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, dto, 1L));
    }

    @Test
    @DisplayName("Should delete category when category exists and has no transactions")
    void deleteCategoryCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Category category = new Category(1L, "Alimentacao", CategoryType.EXPENSE, user);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);

        categoryService.deleteCategory(1L, 1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when category has associated transactions")
    void deleteCategoryCase2() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Category category = new Category(1L, "Alimentacao", CategoryType.EXPENSE, user);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> categoryService.deleteCategory(1L, 1L));
    }
}
