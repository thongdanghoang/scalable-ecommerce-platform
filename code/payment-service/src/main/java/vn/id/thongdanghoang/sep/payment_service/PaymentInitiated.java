package vn.id.thongdanghoang.sep.payment_service;

import java.math.BigDecimal;

public record PaymentInitiated(
        String transactionId,
        String userId,
        BigDecimal originalAmount,
        String voucherCode
) {}
