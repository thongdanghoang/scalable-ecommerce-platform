package vn.id.thongdanghoang.sep.wallet;

public record PromotionApplied(
        String transactionId,
        String userId,
        double originalAmount,
        double discountAmount,
        double finalAmount,
        String appliedVoucher
) {}