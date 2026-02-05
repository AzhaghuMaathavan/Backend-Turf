package com.example.backend.dto.bookingreview;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class BookingReviewResponse {
    Long bookingReviewId;
    Long userId;
    Long bookingId;
    Long productId;
    String productName;
    Integer score;
    String feedbackText;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
