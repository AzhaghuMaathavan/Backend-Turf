package com.example.backend.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequest {

    @Size(max = 150, message = "productName must be <= 150 characters")
    private String productName;

    @Size(max = 2000, message = "description must be <= 2000 characters")
    private String description;

    @Min(value = 0, message = "price must be >= 0")
    private Double price;

    @Min(value = 0, message = "stockQuantity must be >= 0")
    private Integer stockQuantity;

    private Long categoryId;

    @Size(max = 1000, message = "imageUrl must be <= 1000 characters")
    private String imageUrl;
}
