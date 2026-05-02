package com.financas.backend.service.impl;

import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.Category;
import com.financas.backend.entity.CategoryType;
import com.financas.backend.entity.User;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

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
}
