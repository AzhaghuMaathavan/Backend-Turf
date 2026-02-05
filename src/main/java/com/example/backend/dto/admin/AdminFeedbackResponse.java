package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class AdminFeedbackResponse {
	Long feedbackId;
	Integer rating;
	String comment;
	Boolean isAnonymous;
	Integer helpfulCount;
	LocalDateTime createdAt;
	Long productId;
	String productName;
	Long userId;
	String userName;
}
