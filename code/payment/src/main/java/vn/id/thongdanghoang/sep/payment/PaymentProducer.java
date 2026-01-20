package vn.id.thongdanghoang.sep.payment;

import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.scheduler.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * An application that sends messages or events to a message broker topic or queue.
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    @Channel("payment-initiated-topic")
    Emitter<PaymentInitiated> emitter;

    @Scheduled(every = "1s", delayed = "5s")
    void onStart() throws JsonProcessingException {
        var vounchers = Set.of("SAVE50", "FREESHIP", "SAVE10", "NOT_FOUND");
        var min = 1000;
        var max = 5000;
        var delayMillis = random.nextInt(max - min + 1) + min;
        var txId = UUID.randomUUID().toString();
        var amount = delayMillis * 100;
        var applyVouncher = random.nextBoolean();
        var builder = PaymentInitiated.newBuilder()
                .setTransactionId(txId)
                .setUserId("user-" + Instant.now().toEpochMilli())
                .setOriginalAmount(BigDecimal.valueOf(amount));
        if (applyVouncher) {
            builder.setVoucherCode(vounchers.stream().findAny().orElseThrow());
        }
        var event = builder.build();
        log.info(">> [PAYMENT] Init: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        emitter.send(event);
    }
}
