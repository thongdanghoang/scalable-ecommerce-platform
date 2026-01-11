package vn.id.thongdanghoang.sep.payment_service;

public record PromotionApplied(
        String transactionId,
        String userId,
        double originalAmount,
        double discountAmount,
        double finalAmount,
        String appliedVoucher
) {}