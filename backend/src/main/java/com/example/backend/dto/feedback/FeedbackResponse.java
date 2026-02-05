package com.example.backend.dto.feedback;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class FeedbackResponse {
	Long feedbackId;
	Integer rating;
	String comment;
	Boolean isAnonymous;
	Integer helpfulCount; // Advanced: social proof
	LocalDateTime createdAt;
	Long productId;
	Long userId;
	String userName;
}
