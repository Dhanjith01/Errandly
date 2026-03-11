package com.errandly.Errandly.errand.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.errandly.Errandly.errand.errand_state.ErrandState;
import com.errandly.Errandly.payment.entity.Payment;
import com.errandly.Errandly.user.entity.Runner;
import com.errandly.Errandly.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="errands",
    indexes = {
        @Index(name = "idx_errand_status", columnList = "status"),
        @Index(name = "idx_errand_created_at", columnList = "created_at"),
        @Index(name = "idx_errand_runner", columnList = "runner_id"),
        @Index(name = "idx_errand_customer", columnList = "customer_id")
    }
)
public class Errand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private User customer;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "runner_id")
    private Runner runner;

    @Column(nullable=false)
    private String description;
    
    //Errand Status
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ErrandStatus status=ErrandStatus.CREATED;

    @Transient
    private ErrandState state;

    

    //Prices
    @Column(nullable=false)
    private BigDecimal quotedprice;
    private BigDecimal platformFee;
    private BigDecimal gst;
    private BigDecimal totalAmount;

    
    //Time Stamps
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime acceptedAt;
    private LocalDateTime deliveredAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="payment_id")
    Payment payment;
}
