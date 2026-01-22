package vn.id.thongdanghoang.sep.promotion.service;

import vn.id.thongdanghoang.sep.schemas.PaymentFailed;
import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;
import vn.id.thongdanghoang.sep.schemas.PromotionApplied;

import io.smallrye.mutiny.Uni;

public interface PromotionService {

    /**
     * Core Logic: Handles the application of promotional codes.
     * Flow:
     * 1. Validate input.
     * 2. Check if the promotion exists (DB/Cache).
     * 3. Check the condition (Rule Engine).
     * 4. Check and Decrement Inventory (Atomic).
     * 5. Save the PromotionRedemption (Audit Log - PENDING).
     * 6. Return the result for Kafka Producer to send.
     */
    Uni<PromotionApplied> applyPromotion(PaymentInitiated request);

    /**
     * Saga Logic: Undo when Wallet fails to deduct money.
     * Flow:
     * 1. Find PromotionRedemption by transactionId.
     * 2. Update status -> CANCELLED.
     * 3. Increment Inventory (Return voucher to inventory).
     */
    Uni<Void> compensateTransaction(PaymentFailed failureEvent);

    /**
     * Optional: Confirm successful transaction (if 100% accurate tracking is required)
     * When the Wallet successfully deducts funds, it triggers a PaymentSuccess event -> update the status -> CONFIRMED
     */
    Uni<Void> confirmTransaction(String transactionId);
}
