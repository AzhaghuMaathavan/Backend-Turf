package com.example.backend;

import com.example.backend.dto.booking.BookingCreateRequest;
import com.example.backend.dto.bookingreview.BookingReviewUpsertRequest;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.BookingReviewService;
import com.example.backend.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookingReviewServiceTests {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingReviewService bookingReviewService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void upsert_createsOrUpdatesReviewPerBooking() {
        User user = userRepository.findAll().stream().findFirst().orElseThrow();
        Product product = productRepository.findAll().stream().findFirst().orElseThrow();

        var booking = bookingService.create(new BookingCreateRequest(user.getUserId(), product.getProductId(), 1));

        BookingReviewUpsertRequest create = new BookingReviewUpsertRequest();
        create.setUserId(user.getUserId());
        create.setBookingId(booking.getBookingId());
        create.setScore(5);
        create.setFeedbackText("Great turf");

        var r1 = bookingReviewService.upsert(create);
        assertThat(r1.getBookingReviewId()).isNotNull();
        assertThat(r1.getScore()).isEqualTo(5);

        BookingReviewUpsertRequest update = new BookingReviewUpsertRequest();
        update.setUserId(user.getUserId());
        update.setBookingId(booking.getBookingId());
        update.setScore(4);
        update.setFeedbackText("Good");

        var r2 = bookingReviewService.upsert(update);
        assertThat(r2.getBookingReviewId()).isEqualTo(r1.getBookingReviewId());
        assertThat(r2.getScore()).isEqualTo(4);
        assertThat(r2.getFeedbackText()).isEqualTo("Good");
    }
}
