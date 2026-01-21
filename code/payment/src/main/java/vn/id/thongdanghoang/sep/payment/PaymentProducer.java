package vn.id.thongdanghoang.sep.payment;

import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.scheduler.Scheduled;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        var vounchers = List.of("SAVE50", "FREESHIP", "SAVE10", "NOT_FOUND");
        var min = 1000;
        var max = 5000;
        var delayMillis = random.nextInt(max - min + 1) + min;
        var txId = UUID.randomUUID().toString();
        var amount = delayMillis * 100;
        var builder = PaymentInitiated.newBuilder()
                .setTransactionId(txId)
                .setUserId("user-" + Instant.now().toEpochMilli())
                .setOriginalAmount(BigDecimal.valueOf(amount));
        builder.setVoucherCode(vounchers.get(random.nextInt(vounchers.size())));
        var event = builder.build();
        log.info(">> [PAYMENT] Init: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        emitter.send(event);
    }
}
