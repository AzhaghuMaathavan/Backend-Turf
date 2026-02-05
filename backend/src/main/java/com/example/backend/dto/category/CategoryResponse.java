package com.example.backend.dto.category;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryResponse {
    Long categoryId;
    String categoryName;
    String description;
}
