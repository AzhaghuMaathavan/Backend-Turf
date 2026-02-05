package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class AdminRatingResponse {
	Long ratingId;
	Integer score;
	LocalDateTime createdAt;
	Long productId;
	String productName;
	Long userId;
	String userName;
}
