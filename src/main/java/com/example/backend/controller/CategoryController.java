package com.example.backend.controller;

import com.example.backend.dto.category.CategoryCreateRequest;
import com.example.backend.dto.category.CategoryUpdateRequest;
import com.example.backend.dto.category.CategoryResponse;
import com.example.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.list();
    }

    @GetMapping("/{categoryId}")
    public CategoryResponse get(@PathVariable Long categoryId) {
        return categoryService.get(categoryId);
    }

    @PutMapping("/{categoryId}")
    public CategoryResponse update(@PathVariable Long categoryId, @Valid @RequestBody CategoryUpdateRequest request) {
        return categoryService.update(categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }
}
