package com.example.backend.repository;

import com.example.backend.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    Page<Suggestion> findByProduct_ProductId(Long productId, Pageable pageable);

    Page<Suggestion> findByUser_UserId(Long userId, Pageable pageable);
}
