package com.example.backend.service;

import com.example.backend.dto.bookingreview.BookingReviewResponse;
import com.example.backend.dto.bookingreview.BookingReviewUpsertRequest;
import com.example.backend.entity.Booking;
import com.example.backend.entity.BookingReview;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.BookingReviewRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingReviewService {

    private final BookingReviewRepository bookingReviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingReviewResponse upsert(BookingReviewUpsertRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking not found: " + request.getBookingId()));

        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Booking does not belong to this user");
        }

        BookingReview review = bookingReviewRepository.findByBooking_BookingId(booking.getBookingId())
                .map(existing -> {
                    existing.setScore(request.getScore());
                    existing.setFeedbackText(clean(request.getFeedbackText()));
                    return existing;
                })
                .orElseGet(() -> BookingReview.builder()
                        .user(user)
                        .booking(booking)
                        .score(request.getScore())
                        .feedbackText(clean(request.getFeedbackText()))
                        .build());

        return toResponse(bookingReviewRepository.save(review));
    }

    public List<BookingReviewResponse> listByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
        return bookingReviewRepository.findByUser_UserIdOrderByUpdatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private String clean(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private BookingReviewResponse toResponse(BookingReview review) {
        Booking booking = review.getBooking();
        return BookingReviewResponse.builder()
                .bookingReviewId(review.getBookingReviewId())
                .userId(review.getUser().getUserId())
                .bookingId(booking.getBookingId())
                .productId(booking.getProduct().getProductId())
                .productName(booking.getProduct().getProductName())
                .score(review.getScore())
                .feedbackText(review.getFeedbackText())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
