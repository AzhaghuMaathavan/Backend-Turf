package com.example.backend.controller;

import com.example.backend.dto.product.ProductCreateRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.product.ProductUpdateRequest;
import com.example.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
        return productService.create(request);
    }

    @GetMapping
    public Page<ProductResponse> list(
            @PageableDefault(size = 12, sort = "productName", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly
    ) {
        if (q != null && !q.isBlank()) {
            return productService.search(q, pageable);
        }
        if (availableOnly) {
            return productService.listAvailable(pageable);
        }
        return productService.list(pageable);
    }

    @GetMapping("/{productId}")
    public ProductResponse get(@PathVariable Long productId) {
        return productService.get(productId);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable Long productId, @Valid @RequestBody ProductUpdateRequest request) {
        return productService.update(productId, request);
    }

	@DeleteMapping("/{productId}")
	public void delete(@PathVariable Long productId) {
		productService.delete(productId);
	}
}
