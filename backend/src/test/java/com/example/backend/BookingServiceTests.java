package com.example.backend;

import com.example.backend.dto.booking.BookingCreateRequest;
import com.example.backend.dto.booking.BookingResponse;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceTests {

    @Autowired
    BookingService bookingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void createBooking_setsCreatedAt_andReducesAvailability() {
        User user = userRepository.findByEmail("demo@example.com").orElseThrow();
        Product product = productRepository.findByProductName("Basketball Court").orElseThrow();

        int before = product.getStockQuantity();

        BookingResponse booking = bookingService.create(new BookingCreateRequest(
                user.getUserId(),
                product.getProductId(),
                2
        ));

        assertNotNull(booking.getBookingId());
        assertNotNull(booking.getCreatedAt());
        assertEquals(2, booking.getDurationHours());
        assertEquals(product.getPrice() * 2, booking.getAmount());

        Product after = productRepository.findById(product.getProductId()).orElseThrow();
        assertEquals(before - 2, after.getStockQuantity());
    }
}
