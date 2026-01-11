package vn.id.thongdanghoang.sep.promotion;

public record PaymentInitiated(
        String transactionId,
        String userId,
        double originalAmount,
        String voucherCode
) {}
