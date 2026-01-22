package vn.id.thongdanghoang.sep.promotion.entity;

import vn.id.thongdanghoang.sep.promotion.type.RedemptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "promotion_redemptions", indexes = {
        @Index(name = "idx_transaction_id", columnList = "transactionId", unique = true),
        @Index(name = "idx_user_id", columnList = "userId")
})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionRedemption extends BaseEntity {

    @NotNull
    @Column(nullable = false, unique = true)
    private String transactionId;

    @NotNull
    private String userId;

    @ManyToOne
    // @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @NotNull
    private BigDecimal originalAmount;

    @NotNull
    private BigDecimal discountAmount;

    @NotNull
    private BigDecimal finalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RedemptionStatus status;

    @Column(updatable = false)
    private Instant createdDate;

    private Instant lastModifiedDate;

    @PrePersist
    void onCreate() {
        this.createdDate = Instant.now();
        this.lastModifiedDate = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.lastModifiedDate = Instant.now();
    }
}
