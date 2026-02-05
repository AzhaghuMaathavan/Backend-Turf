package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class AdminBookingReviewResponse {
	Long bookingReviewId;
	Long bookingId;
	Long userId;
	String userName;
	String userEmail;
	Long productId;
	String productName;
	Integer score;
	String feedbackText;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
