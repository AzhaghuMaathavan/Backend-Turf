package com.example.backend.dto.bookingreview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookingReviewUpsertRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long bookingId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    @Size(max = 2000)
    private String feedbackText;
}
