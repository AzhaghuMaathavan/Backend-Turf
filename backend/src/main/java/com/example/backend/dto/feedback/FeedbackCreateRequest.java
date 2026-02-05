package com.example.backend.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackCreateRequest {

	@NotNull(message = "userId is required")
	private Long userId;

	@NotNull(message = "productId is required")
	private Long productId;

	@NotNull(message = "rating is required")
	@Min(value = 1, message = "rating must be between 1 and 5")
	@Max(value = 5, message = "rating must be between 1 and 5")
	private Integer rating;

	@Size(max = 4000, message = "comment must be <= 4000 characters")
	private String comment;

	private Boolean isAnonymous = false;
}
