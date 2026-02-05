package com.example.backend.service;

import com.example.backend.dto.category.CategoryResponse;
import com.example.backend.dto.product.ProductCreateRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.product.ProductUpdateRequest;
import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Category category = categoryService.getEntityOrThrow(request.getCategoryId());

        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public Page<ProductResponse> list(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    public Page<ProductResponse> listAvailable(Pageable pageable) {
        return productRepository.findAvailableProducts(pageable).map(this::toResponse);
    }

    public Page<ProductResponse> search(String q, Pageable pageable) {
        return productRepository.searchProducts(q, pageable).map(this::toResponse);
    }

    public ProductResponse get(Long productId) {
        return toResponse(getEntityOrThrow(productId));
    }

    @Transactional
    public ProductResponse update(Long productId, ProductUpdateRequest request) {
        Product product = getEntityOrThrow(productId);

        if (request.getProductName() != null) product.setProductName(request.getProductName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getCategoryId() != null) {
            Category category = categoryService.getEntityOrThrow(request.getCategoryId());
            product.setCategory(category);
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long productId) {
        Product product = getEntityOrThrow(productId);
        productRepository.delete(product);
    }

    public Product getEntityOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
    }

    private ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        CategoryResponse categoryResponse = category == null ? null : CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();

        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .averageRating(product.getAverageRating()) // Advanced: computed field
                .category(categoryResponse)
                .build();
    }
}
