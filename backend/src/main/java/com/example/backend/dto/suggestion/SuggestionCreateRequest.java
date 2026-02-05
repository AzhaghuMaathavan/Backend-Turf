package com.example.backend.dto.suggestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SuggestionCreateRequest {
	@NotNull(message = "userId is required")
	private Long userId;

	@NotNull(message = "productId is required")
	private Long productId;

	@NotBlank(message = "suggestionText is required")
	@Size(max = 4000, message = "suggestionText must be <= 4000 characters")
	private String suggestionText;
}
