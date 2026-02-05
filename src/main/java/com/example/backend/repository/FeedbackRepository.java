package com.example.backend.repository;

import com.example.backend.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProduct_ProductId(Long productId);
    
    Page<Feedback> findByProduct_ProductId(Long productId, Pageable pageable);
    
    Page<Feedback> findByUser_UserId(Long userId, Pageable pageable);
    
    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
    
    @Query("SELECT f FROM Feedback f WHERE f.product.productId = :productId AND f.isAnonymous = false ORDER BY f.helpfulCount DESC")
    List<Feedback> findMostHelpfulFeedbackForProduct(@Param("productId") Long productId);
}
