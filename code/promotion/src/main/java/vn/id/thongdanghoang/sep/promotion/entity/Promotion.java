package vn.id.thongdanghoang.sep.promotion.entity;

import vn.id.thongdanghoang.sep.promotion.type.DiscountType;
import vn.id.thongdanghoang.sep.promotion.type.PromotionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "promotions")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion extends OptimisticEntity {

    @NotBlank(message = "Code cannot be blank")
    @Size(min = 3, max = 20, message = "Code length must be between 3 and 20")
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @NotNull
    @Positive(message = "Value must be positive")
    private BigDecimal discountValue;

    @PositiveOrZero
    private BigDecimal maxDiscountAmount;

    @NotNull
    // @Future(message = "End date must be in the future") -> only applied on create
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PromotionStatus status;

    @Min(value = 0, message = "Total usage limit cannot be negative")
    private int totalUsageLimit;

    @Min(value = 0)
    private int currentUsageCount;
}
