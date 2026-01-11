package vn.id.thongdanghoang.sep.promotion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PromotionProcessor {

    private final ObjectMapper objectMapper;

    @Incoming("payment-in")
    @Outgoing("promotion-out")
    public Uni<Message<PromotionApplied>> process(Message<PaymentInitiated> msg) {
        PaymentInitiated payload = msg.getPayload();

        // Simulate Async Logic (e.g., Calling Redis)
        return Uni.createFrom().item(payload)
                .map(p -> {
                    double discount = 0.0;
                    String status = "NONE";

                    // Simple Logic Engine
                    if ("SAVE50".equals(p.voucherCode())) {
                        discount = p.originalAmount() * 0.5;
                        status = "APPLIED";
                    }

                    double finalAmount = p.originalAmount() - discount;

                    var result = new PromotionApplied(
                            p.transactionId(),
                            p.userId(),
                            p.originalAmount(),
                            discount,
                            finalAmount,
                            status);

                    try {
                        log.info("|| [PROMO] Processed: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
                    } catch (JsonProcessingException e) {
                        log.error("Error processing transaction: {}", e.getMessage(), e);
                    }
                    return Message.of(result);
                })
                .onItem().invoke(() -> msg.ack());
    }
}
