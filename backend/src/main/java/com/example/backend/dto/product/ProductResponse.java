package com.example.backend.dto.product;

import com.example.backend.dto.category.CategoryResponse;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductResponse {
    Long productId;
    String productName;
    String description;
    Double price;
    Integer stockQuantity;
    String imageUrl;
    Double averageRating; // Advanced: derived field
    CategoryResponse category;
}
