package com.example.backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequest {

    @NotBlank(message = "categoryName is required")
    @Size(max = 100, message = "categoryName must be <= 100 characters")
    private String categoryName;

    @Size(max = 500, message = "description must be <= 500 characters")
    private String description;
}
