package com.example.backend.controller;

import com.example.backend.dto.suggestion.SuggestionCreateRequest;
import com.example.backend.dto.suggestion.SuggestionResponse;
import com.example.backend.service.SuggestionService;
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
public class SuggestionController {

	private final SuggestionService suggestionService;

	@PostMapping("/suggestions")
	public SuggestionResponse create(@Valid @RequestBody SuggestionCreateRequest request) {
		return suggestionService.create(request);
	}

	@GetMapping("/products/{productId}/suggestions")
	public Page<SuggestionResponse> listByProduct(
			@PathVariable Long productId,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return suggestionService.listByProduct(productId, pageable);
	}
}
