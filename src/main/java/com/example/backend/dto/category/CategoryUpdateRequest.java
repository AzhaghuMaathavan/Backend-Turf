package com.example.backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(
		@NotBlank(message = "categoryName is required")
		@Size(max = 100, message = "categoryName must be <= 100 chars")
		String categoryName,

		@Size(max = 500, message = "description must be <= 500 chars")
		String description
) {
}
