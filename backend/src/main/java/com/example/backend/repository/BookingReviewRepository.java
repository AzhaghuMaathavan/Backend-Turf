package com.example.backend.repository;

import com.example.backend.entity.BookingReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingReviewRepository extends JpaRepository<BookingReview, Long> {
    Optional<BookingReview> findByBooking_BookingId(Long bookingId);

    List<BookingReview> findByUser_UserIdOrderByUpdatedAtDesc(Long userId);
}
