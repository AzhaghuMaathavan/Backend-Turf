package com.example.backend.controller;

import com.example.backend.dto.bookingreview.BookingReviewResponse;
import com.example.backend.dto.bookingreview.BookingReviewUpsertRequest;
import com.example.backend.service.BookingReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking-reviews")
public class BookingReviewController {

    private final BookingReviewService bookingReviewService;

    @PostMapping
    public BookingReviewResponse upsert(@Valid @RequestBody BookingReviewUpsertRequest request) {
        return bookingReviewService.upsert(request);
    }

    @GetMapping
    public List<BookingReviewResponse> listByUser(@RequestParam Long userId) {
        return bookingReviewService.listByUser(userId);
    }
}
