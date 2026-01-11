package vn.id.thongdanghoang.sep.wallet;

public record PaymentInitiated(
        String transactionId,
        String userId,
        double originalAmount,
        String voucherCode
) {}
