package com.example.backend.service;

import com.example.backend.dto.admin.*;
import com.example.backend.entity.Booking;
import com.example.backend.entity.BookingReview;
import com.example.backend.entity.Feedback;
import com.example.backend.entity.Rating;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final BookingRepository bookingRepository;
	private final BookingReviewRepository bookingReviewRepository;
	private final FeedbackRepository feedbackRepository;
	private final RatingRepository ratingRepository;

	private User requireAdmin(Long adminUserId) {
		User user = userRepository.findById(adminUserId)
				.orElseThrow(() -> new NotFoundException("User not found: " + adminUserId));
		if (user.getRole() != User.UserRole.ADMIN) {
			throw new BadRequestException("Admin access required");
		}
		return user;
	}

	@Transactional(readOnly = true)
	public AdminDashboardResponse dashboard(Long adminUserId) {
		requireAdmin(adminUserId);

		long totalUsers = userRepository.count();
		long totalProducts = productRepository.count();
		long totalBookings = bookingRepository.count();
		long totalBookingReviews = bookingReviewRepository.count();
		long totalFeedbacks = feedbackRepository.count();
		long totalRatings = ratingRepository.count();

		List<Booking> bookings = bookingRepository.findAll();
		LocalDate today = LocalDate.now();
		long bookingsToday = bookings.stream()
				.filter(b -> b.getCreatedAt() != null && b.getCreatedAt().toLocalDate().equals(today))
				.count();

		long pendingReviews = Math.max(0, totalBookings - totalBookingReviews);

		YearMonth currentMonth = YearMonth.now();
		double monthlyRevenue = bookings.stream()
				.filter(b -> b.getCreatedAt() != null)
				.filter(b -> YearMonth.from(b.getCreatedAt()).equals(currentMonth))
				.filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
				.mapToDouble(b -> b.getAmount() == null ? 0.0 : b.getAmount())
				.sum();

		List<AdminTimeseriesPoint> completionTrend = computeCompletionRateTrend(bookings, 6);
		List<AdminPieSlice> revenueByType = computeRevenueByType(bookings, currentMonth);

		return AdminDashboardResponse.builder()
				.totalUsers(totalUsers)
				.totalProducts(totalProducts)
				.totalBookings(totalBookings)
				.totalBookingReviews(totalBookingReviews)
				.totalFeedbacks(totalFeedbacks)
				.totalRatings(totalRatings)
				.bookingsToday(bookingsToday)
				.pendingReviews(pendingReviews)
				.monthlyRevenue(monthlyRevenue)
				.completionRateTrend(completionTrend)
				.revenueByTurfType(revenueByType)
				.build();
	}

	@Transactional(readOnly = true)
	public List<AdminBookingReviewResponse> listBookingReviews(Long adminUserId, int limit) {
		requireAdmin(adminUserId);
		int safeLimit = Math.max(1, Math.min(limit, 500));

		List<BookingReview> all = bookingReviewRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
		List<AdminBookingReviewResponse> out = new ArrayList<>();
		for (BookingReview r : all) {
			if (out.size() >= safeLimit) break;
			out.add(AdminBookingReviewResponse.builder()
					.bookingReviewId(r.getBookingReviewId())
					.bookingId(r.getBooking().getBookingId())
					.userId(r.getUser().getUserId())
					.userName(r.getUser().getUserName())
					.userEmail(r.getUser().getEmail())
					.productId(r.getBooking().getProduct().getProductId())
					.productName(r.getBooking().getProduct().getProductName())
					.score(r.getScore())
					.feedbackText(r.getFeedbackText())
					.createdAt(r.getCreatedAt())
					.updatedAt(r.getUpdatedAt())
					.build());
		}
		return out;
	}

	@Transactional(readOnly = true)
	public Page<AdminFeedbackResponse> listFeedback(Long adminUserId, Pageable pageable) {
		requireAdmin(adminUserId);
		return feedbackRepository.findAll(pageable).map(this::toAdminFeedback);
	}

	@Transactional(readOnly = true)
	public Page<AdminRatingResponse> listRatings(Long adminUserId, Pageable pageable) {
		requireAdmin(adminUserId);
		return ratingRepository.findAll(pageable).map(this::toAdminRating);
	}

	private AdminFeedbackResponse toAdminFeedback(Feedback f) {
		return AdminFeedbackResponse.builder()
				.feedbackId(f.getFeedbackId())
				.rating(f.getRating())
				.comment(f.getComment())
				.isAnonymous(f.getIsAnonymous())
				.helpfulCount(f.getHelpfulCount())
				.createdAt(f.getCreatedAt())
				.productId(f.getProduct().getProductId())
				.productName(f.getProduct().getProductName())
				.userId(Boolean.TRUE.equals(f.getIsAnonymous()) ? null : f.getUser().getUserId())
				.userName(Boolean.TRUE.equals(f.getIsAnonymous()) ? null : f.getUser().getUserName())
				.build();
	}

	private AdminRatingResponse toAdminRating(Rating r) {
		return AdminRatingResponse.builder()
				.ratingId(r.getRatingId())
				.score(r.getScore())
				.createdAt(r.getCreatedAt())
				.productId(r.getProduct().getProductId())
				.productName(r.getProduct().getProductName())
				.userId(r.getUser().getUserId())
				.userName(r.getUser().getUserName())
				.build();
	}

	private List<AdminTimeseriesPoint> computeCompletionRateTrend(List<Booking> bookings, int months) {
		int m = Math.max(1, Math.min(months, 24));
		YearMonth now = YearMonth.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
		List<AdminTimeseriesPoint> points = new ArrayList<>();

		for (int i = m - 1; i >= 0; i--) {
			YearMonth ym = now.minusMonths(i);
			long total = bookings.stream()
					.filter(b -> b.getCreatedAt() != null)
					.filter(b -> YearMonth.from(b.getCreatedAt()).equals(ym))
					.count();
			long confirmed = bookings.stream()
					.filter(b -> b.getCreatedAt() != null)
					.filter(b -> YearMonth.from(b.getCreatedAt()).equals(ym))
					.filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
					.count();

			double rate = total == 0 ? 0.0 : (confirmed * 100.0) / total;
			points.add(AdminTimeseriesPoint.builder()
					.label(ym.format(fmt))
					.completionRate(rate)
					.totalBookings(total)
					.build());
		}
		return points;
	}

	private List<AdminPieSlice> computeRevenueByType(List<Booking> bookings, YearMonth currentMonth) {
		Map<String, Double> totals = new HashMap<>();
		for (Booking b : bookings) {
			if (b.getCreatedAt() == null) continue;
			if (!YearMonth.from(b.getCreatedAt()).equals(currentMonth)) continue;
			if (b.getStatus() != Booking.BookingStatus.CONFIRMED) continue;
			String name = b.getProduct() != null ? b.getProduct().getProductName() : "Unknown";
			double amount = b.getAmount() == null ? 0.0 : b.getAmount();
			totals.put(name, totals.getOrDefault(name, 0.0) + amount);
		}

		// If this month has no revenue yet, fall back to all-time.
		if (totals.isEmpty()) {
			for (Booking b : bookings) {
				if (b.getStatus() != Booking.BookingStatus.CONFIRMED) continue;
				String name = b.getProduct() != null ? b.getProduct().getProductName() : "Unknown";
				double amount = b.getAmount() == null ? 0.0 : b.getAmount();
				totals.put(name, totals.getOrDefault(name, 0.0) + amount);
			}
		}

		return totals.entrySet().stream()
				.map(e -> AdminPieSlice.builder().name(e.getKey()).value(e.getValue()).build())
				.sorted(Comparator.comparing(AdminPieSlice::getValue).reversed())
				.toList();
	}
}
