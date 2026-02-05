package com.example.backend.service;

import com.example.backend.dto.suggestion.SuggestionCreateRequest;
import com.example.backend.dto.suggestion.SuggestionResponse;
import com.example.backend.entity.Product;
import com.example.backend.entity.Suggestion;
import com.example.backend.entity.User;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.SuggestionRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestionService {

	private final SuggestionRepository suggestionRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	@Transactional
	public SuggestionResponse create(SuggestionCreateRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

		Suggestion suggestion = Suggestion.builder()
				.user(user)
				.product(product)
				.suggestionText(request.getSuggestionText())
				.status(Suggestion.SuggestionStatus.PENDING)
				.upvoteCount(0)
				.build();

		return toResponse(suggestionRepository.save(suggestion));
	}

	public Page<SuggestionResponse> listByProduct(Long productId, Pageable pageable) {
		if (!productRepository.existsById(productId)) {
			throw new NotFoundException("Product not found: " + productId);
		}
		return suggestionRepository.findByProduct_ProductId(productId, pageable).map(this::toResponse);
	}

	private SuggestionResponse toResponse(Suggestion suggestion) {
		return SuggestionResponse.builder()
				.suggestionId(suggestion.getSuggestionId())
				.suggestionText(suggestion.getSuggestionText())
				.status(suggestion.getStatus())
				.adminResponse(suggestion.getAdminResponse())
				.upvoteCount(suggestion.getUpvoteCount())
				.createdAt(suggestion.getCreatedAt())
				.productId(suggestion.getProduct().getProductId())
				.userId(suggestion.getUser().getUserId())
				.userName(suggestion.getUser().getUserName())
				.build();
	}
}
