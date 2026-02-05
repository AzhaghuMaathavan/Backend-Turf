package com.example.backend.repository;

import com.example.backend.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findByProduct_ProductId(Long productId, Pageable pageable);

    Optional<Rating> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}
