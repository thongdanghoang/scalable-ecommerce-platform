package vn.id.thongdanghoang.sep.payment_service;

import java.math.BigDecimal;

public record PromotionApplied(
        String transactionId,
        String userId,
        BigDecimal originalAmount,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        String appliedVoucher
) {}