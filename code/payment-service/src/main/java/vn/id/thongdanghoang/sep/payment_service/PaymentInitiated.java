package vn.id.thongdanghoang.sep.payment_service;

public record PaymentInitiated(
        String transactionId,
        String userId,
        double originalAmount,
        String voucherCode
) {}
