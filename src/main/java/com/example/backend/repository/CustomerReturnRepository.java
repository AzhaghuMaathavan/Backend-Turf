package com.example.backend.repository;

import com.example.backend.entity.CustomerReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReturnRepository extends JpaRepository<CustomerReturn, Long> {
    Page<CustomerReturn> findByUser_UserId(Long userId, Pageable pageable);

    Page<CustomerReturn> findByProduct_ProductId(Long productId, Pageable pageable);
}
