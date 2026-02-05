package com.example.backend.repository;

import com.example.backend.entity.PurchaseOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
    Page<PurchaseOrderItem> findByPurchaseOrder_PurchaseOrderId(Long purchaseOrderId, Pageable pageable);
}
