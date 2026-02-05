package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AdminDashboardResponse {
	Long totalUsers;
	Long totalProducts;
	Long totalBookings;
	Long totalBookingReviews;
	Long totalFeedbacks;
	Long totalRatings;

	Long bookingsToday;
	Long pendingReviews;
	Double monthlyRevenue;

	List<AdminTimeseriesPoint> completionRateTrend;
	List<AdminPieSlice> revenueByTurfType;
}
