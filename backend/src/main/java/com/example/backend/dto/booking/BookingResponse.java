package com.example.backend.dto.booking;

import com.example.backend.entity.Booking;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class BookingResponse {
    Long bookingId;
    Long userId;
    Long productId;
    String productName;
    Integer durationHours;
    Double amount;
    Booking.BookingStatus status;
    LocalDateTime createdAt;
}
