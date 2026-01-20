package vn.id.thongdanghoang.sep.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.scheduler.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    @Channel("payment-initiated-topic")
    Emitter<PaymentInitiated> emitter;

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    @Scheduled(every = "5s", delayed = "5s")
    void onStart() throws JsonProcessingException {
        var min = 1000;
        var max = 5000;
        var delayMillis = random.nextInt(max - min + 1) + min;
        var txId = UUID.randomUUID().toString();
        var amount = delayMillis * 100; // 100k - 500k
        var voucher = random.nextBoolean() ? "SAVE50" : "FREESHIP"; // Random voucher
        var event = new PaymentInitiated(txId, "user-" + Instant.now().toEpochMilli(), BigDecimal.valueOf(amount), voucher);
        log.info(">> [PAYMENT] Init: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        emitter.send(event);
    }
}
