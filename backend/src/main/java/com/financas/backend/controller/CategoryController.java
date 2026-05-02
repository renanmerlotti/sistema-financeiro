package com.financas.backend.controller;

import com.financas.backend.dto.request.CategoryRequestDTO;
import com.financas.backend.dto.response.CategoryResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
            @AuthenticationPrincipal User user) {
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO, user.getId());
        return ResponseEntity.status(201).body(categoryResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listCategories(@AuthenticationPrincipal User user) {
        List<CategoryResponseDTO> categories = categoryService.listCategoriesByUserId(user.getId());
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long categoryId,
                                               @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(categoryId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
