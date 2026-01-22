package vn.id.thongdanghoang.sep.promotion;

import vn.id.thongdanghoang.sep.promotion.service.PromotionService;
import vn.id.thongdanghoang.sep.schemas.PaymentFailed;
import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;
import vn.id.thongdanghoang.sep.schemas.PaymentSuccess;
import vn.id.thongdanghoang.sep.schemas.PromotionApplied;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * An application that acts as both a consumer and a producer, consuming messages from one topic, applying business logic or
 * transformation, and then producing a new message to a different topic or queue.
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PromotionProcessor {

    private final PromotionService promotionService;

    @Incoming(Channels.PAYMENT_IN)
    @Outgoing(Channels.PROMOTION_OUT)
    public Uni<Message<PromotionApplied>> process(Message<PaymentInitiated> msg) {
        var payload = msg.getPayload();

        log.info(">>Received Tx: {}", payload.getTransactionId());

        return promotionService.applyPromotion(payload)
                .invoke(PromotionProcessor::logAppliedPromoStatus)
                .map(result -> Message.of(result).withAck(msg::ack));
    }

    private static void logAppliedPromoStatus(PromotionApplied result) {
        log.info(
                "||Applied: status={}, voucher={}, final={}",
                result.getStatus(), result.getAppliedVoucher(), result.getFinalAmount());
    }

    @Incoming(Channels.PAYMENT_FAILED_IN)
    public Uni<Void> onPaymentFailed(Message<PaymentFailed> msg) {
        var payload = msg.getPayload();
        log.warn("<<Compensation trigger for Tx: {}", payload.getTransactionId());
        return promotionService.compensateTransaction(payload)
                .invoke(() -> msg.ack())
                .onFailure().recoverWithUni(e -> {
                    log.error("Failed to compensate tx: {}", payload.getTransactionId(), e);
                    return Uni.createFrom().completionStage(msg.nack(e));
                });
    }

    @Incoming(Channels.PAYMENT_SUCCESS_IN)
    public Uni<Void> onPaymentSuccess(Message<PaymentSuccess> msg) {
        var txId = msg.getPayload().getTransactionId();
        return promotionService.confirmTransaction(txId)
                .onFailure().invoke(e -> log.error("Failed to confirm tx: {}", txId, e))
                .invoke(() -> msg.ack());
    }
}
