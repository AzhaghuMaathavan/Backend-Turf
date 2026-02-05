package com.example.backend.controller;

import com.example.backend.dto.feedback.FeedbackCreateRequest;
import com.example.backend.dto.feedback.FeedbackResponse;
import com.example.backend.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedbackController {

	private final FeedbackService feedbackService;

	@PostMapping("/feedback")
	public FeedbackResponse create(@Valid @RequestBody FeedbackCreateRequest request) {
		return feedbackService.create(request);
	}

	@GetMapping("/products/{productId}/feedback")
	public Page<FeedbackResponse> listByProduct(
			@PathVariable Long productId,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return feedbackService.listByProduct(productId, pageable);
	}
}
