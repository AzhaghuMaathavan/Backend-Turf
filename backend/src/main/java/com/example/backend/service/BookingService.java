package com.example.backend.service;

import com.example.backend.dto.booking.BookingCreateRequest;
import com.example.backend.dto.booking.BookingResponse;
import com.example.backend.entity.Booking;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

        int durationHours = request.getDurationHours();
        int available = product.getStockQuantity() == null ? 0 : product.getStockQuantity();

        // Treat stockQuantity as available slot-units; booking consumes durationHours units.
        if (available < durationHours) {
            throw new BadRequestException("Not enough availability for the selected duration");
        }

        double perHour = product.getPrice() == null ? 0.0 : product.getPrice();
        double amount = perHour * durationHours;

        product.setStockQuantity(available - durationHours);
        productRepository.save(product);

        Booking booking = Booking.builder()
                .user(user)
                .product(product)
                .durationHours(durationHours)
                .amount(amount)
            .status(Booking.BookingStatus.CONFIRMED)
            .createdAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    public Page<BookingResponse> listByUser(Long userId, Pageable pageable) {
        return bookingRepository.findByUser_UserId(userId, pageable).map(this::toResponse);
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUser().getUserId())
                .productId(booking.getProduct().getProductId())
                .productName(booking.getProduct().getProductName())
                .durationHours(booking.getDurationHours())
                .amount(booking.getAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
