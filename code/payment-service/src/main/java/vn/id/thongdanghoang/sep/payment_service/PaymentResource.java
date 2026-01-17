package vn.id.thongdanghoang.sep.payment_service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PaymentResource {

    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    @Channel("payment-initiated-topic")
    Emitter<PaymentInitiated> emitter;

    @Scheduled(every = "1s", delayed = "1s")
    void sendPaymentEvent() throws JsonProcessingException {
        var min = 1000;
        var max = 2000;
        var delayMillis = random.nextInt(max - min + 1) + min;
        var txId = UUID.randomUUID().toString();
        var amount = delayMillis * 100; // 100k - 200k
        var voucher = random.nextBoolean() ? "SAVE50" : "FREESHIP"; // Random voucher

        var event = new PaymentInitiated(txId, "user-" + Instant.now(), amount, voucher);
        log.info(">> [PAYMENT] Init: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        emitter.send(event);
    }
}
