package com.example.backend.service;

import com.example.backend.dto.category.CategoryCreateRequest;
import com.example.backend.dto.category.CategoryUpdateRequest;
import com.example.backend.dto.category.CategoryResponse;
import com.example.backend.entity.Category;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryCreateRequest request) {
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new BadRequestException("Category already exists: " + request.getCategoryName());
        }

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .build();

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public List<CategoryResponse> list() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryName"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse get(Long categoryId) {
        return toResponse(getEntityOrThrow(categoryId));
    }

    public CategoryResponse update(Long categoryId, CategoryUpdateRequest request) {
        Category category = getEntityOrThrow(categoryId);
        String newName = request.categoryName();
        if (!category.getCategoryName().equalsIgnoreCase(newName)
                && categoryRepository.existsByCategoryName(newName)) {
            throw new BadRequestException("Category already exists: " + newName);
        }

        category.setCategoryName(newName);
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long categoryId) {
        Category category = getEntityOrThrow(categoryId);
        categoryRepository.delete(category);
    }

    public Category getEntityOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + categoryId));
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}
