package vn.id.thongdanghoang.sep.promotion;

public record PromotionApplied(
        String transactionId,
        String userId,
        double originalAmount,
        double discountAmount,
        double finalAmount,
        String appliedVoucher
) {}