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

import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;
import vn.id.thongdanghoang.sep.schemas.PromotionApplied;

import java.math.BigDecimal;

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
                    var discount = BigDecimal.ZERO;
                    String status = "NONE";

                    // Simple Logic Engine
                    if ("SAVE50".equals(p.getVoucherCode())) {
                        discount = p.getOriginalAmount().multiply(BigDecimal.valueOf(0.5));
                        status = "APPLIED";
                    }

                    var finalAmount = p.getOriginalAmount().subtract(discount);

                    var result = new PromotionApplied(
                            p.getTransactionId(),
                            p.getUserId(),
                            p.getOriginalAmount(),
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
