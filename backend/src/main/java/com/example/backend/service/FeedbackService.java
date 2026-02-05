package com.example.backend.service;

import com.example.backend.dto.feedback.FeedbackCreateRequest;
import com.example.backend.dto.feedback.FeedbackResponse;
import com.example.backend.entity.Feedback;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.FeedbackRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	@Transactional
	public FeedbackResponse create(FeedbackCreateRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new NotFoundException("User not found: " + request.getUserId()));
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

		// Advanced: enforce one feedback per user per product
		if (feedbackRepository.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), product.getProductId())) {
			throw new BadRequestException("Feedback already exists for this user and product");
		}

		Feedback feedback = Feedback.builder()
				.user(user)
				.product(product)
				.rating(request.getRating())
				.comment(request.getComment())
				.isAnonymous(Boolean.TRUE.equals(request.getIsAnonymous()))
				.helpfulCount(0)
				.build();

		return toResponse(feedbackRepository.save(feedback));
	}

	public Page<FeedbackResponse> listByProduct(Long productId, Pageable pageable) {
		if (!productRepository.existsById(productId)) {
			throw new NotFoundException("Product not found: " + productId);
		}
		return feedbackRepository.findByProduct_ProductId(productId, pageable).map(this::toResponse);
	}

	private FeedbackResponse toResponse(Feedback feedback) {
		String userName = Boolean.TRUE.equals(feedback.getIsAnonymous()) ? null : feedback.getUser().getUserName();
		Long userId = Boolean.TRUE.equals(feedback.getIsAnonymous()) ? null : feedback.getUser().getUserId();

		return FeedbackResponse.builder()
				.feedbackId(feedback.getFeedbackId())
				.rating(feedback.getRating())
				.comment(feedback.getComment())
				.isAnonymous(feedback.getIsAnonymous())
				.helpfulCount(feedback.getHelpfulCount())
				.createdAt(feedback.getCreatedAt())
				.productId(feedback.getProduct().getProductId())
				.userId(userId)
				.userName(userName)
				.build();
	}
}
