package com.example.backend.dto.booking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class BookingCreateRequest {
    @NotNull
    Long userId;

    @NotNull
    Long productId;

    @NotNull
    @Min(1)
    @Max(6)
    Integer durationHours;
}
