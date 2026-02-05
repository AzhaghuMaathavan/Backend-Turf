package com.example.backend.dto.suggestion;

import com.example.backend.entity.Suggestion;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class SuggestionResponse {
	Long suggestionId;
	String suggestionText;
	Suggestion.SuggestionStatus status;
	String adminResponse; // Advanced: admin workflow
	Integer upvoteCount; // Advanced: voting
	LocalDateTime createdAt;
	Long productId;
	Long userId;
	String userName;
}
