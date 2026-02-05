package com.example.backend.controller;

import com.example.backend.dto.admin.*;
import com.example.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/dashboard")
	public AdminDashboardResponse dashboard(@RequestParam Long adminUserId) {
		return adminService.dashboard(adminUserId);
	}

	@GetMapping("/booking-reviews")
	public List<AdminBookingReviewResponse> bookingReviews(
			@RequestParam Long adminUserId,
			@RequestParam(defaultValue = "100") int limit
	) {
		return adminService.listBookingReviews(adminUserId, limit);
	}

	@GetMapping("/feedback")
	public Page<AdminFeedbackResponse> feedback(
			@RequestParam Long adminUserId,
			@PageableDefault(size = 25, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return adminService.listFeedback(adminUserId, pageable);
	}

	@GetMapping("/ratings")
	public Page<AdminRatingResponse> ratings(
			@RequestParam Long adminUserId,
			@PageableDefault(size = 25, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return adminService.listRatings(adminUserId, pageable);
	}
}
