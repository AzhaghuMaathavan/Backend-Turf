package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReturn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnReason reason; // DEFECTIVE, NOT_AS_DESCRIBED, CHANGED_MIND, DAMAGED_IN_SHIPMENT, OTHER
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReturnStatus status = ReturnStatus.REQUESTED; // REQUESTED, APPROVED, REJECTED, REFUNDED
    
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column
    private Double refundAmount;
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum ReturnReason {
        DEFECTIVE,
        NOT_AS_DESCRIBED,
        CHANGED_MIND,
        DAMAGED_IN_SHIPMENT,
        OTHER
    }
    
    public enum ReturnStatus {
        REQUESTED,
        APPROVED,
        REJECTED,
        REFUNDED
    }
}
