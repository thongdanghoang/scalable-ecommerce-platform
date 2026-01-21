package vn.id.thongdanghoang.sep.wallet;

import vn.id.thongdanghoang.sep.schemas.PaymentFailed;
import vn.id.thongdanghoang.sep.schemas.PaymentSuccess;
import vn.id.thongdanghoang.sep.schemas.PromotionApplied;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CompletionStage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * An application that retrieves and processes messages from a topic or queue.
 */
@ApplicationScoped
@Slf4j
public class WalletConsumer {

    private final Random random = new Random();

    @ConfigProperty(name = "wallet.simulation.failure-ratio", defaultValue = "0.2")
    double failureRatio;

    @Inject
    @Channel("payment-success-out")
    Emitter<PaymentSuccess> successEmitter;

    @Inject
    @Channel("payment-failed-out")
    Emitter<PaymentFailed> failedEmitter;

    @Incoming("promotion-in")
    public CompletionStage<Void> processTransaction(Message<PromotionApplied> msg) {
        PromotionApplied event = msg.getPayload();
        log.info("<< [WALLET] Receiving Request for User: {}, Amount: {}", event.getUserId(), event.getFinalAmount());

        if (shouldFail()) {
            return processFailure(event, msg);
        } else {
            return processSuccess(event, msg);
        }
    }

    private boolean shouldFail() {
        return random.nextDouble() < failureRatio;
    }

    private CompletionStage<Void> processFailure(PromotionApplied event, Message<PromotionApplied> msg) {
        log.warn("XX [WALLET] INSUFFICIENT FUNDS! Transaction: {}", event.getTransactionId());

        var failedEvent = PaymentFailed.newBuilder()
                .setTransactionId(event.getTransactionId())
                .setUserId(event.getUserId())
                .setAmount(event.getFinalAmount())
                .setReason("INSUFFICIENT_FUNDS_SIMULATED")
                .setVoucherCode(event.getAppliedVoucher())
                .setPromotionStatus(event.getStatus() != null ? event.getStatus().toString() : "NONE")
                .build();

        return failedEmitter.send(failedEvent)
                .handle((ignored, ex) -> {
                    if (ex != null) {
                        log.error("Failed to emit PaymentFailed", ex);
                        return msg.nack(ex);
                    }
                    return msg.ack();
                })
                .thenCompose(stage -> stage);
    }

    private CompletionStage<Void> processSuccess(PromotionApplied event, Message<PromotionApplied> msg) {
        log.info("$$ [WALLET] CHARGED SUCCESS: {}", event.getFinalAmount());

        var successEvent = PaymentSuccess.newBuilder()
                .setTransactionId(event.getTransactionId())
                .setUserId(event.getUserId())
                .setFinalAmount(event.getFinalAmount())
                .setTimestamp(Instant.now())
                .build();

        return successEmitter.send(successEvent)
                .handle((ignored, ex) -> {
                    if (ex != null) {
                        log.error("Failed to emit PaymentSuccess", ex);
                        return msg.nack(ex);
                    }
                    return msg.ack();
                })
                .thenCompose(stage -> stage);
    }
}
