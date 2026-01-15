package vn.id.thongdanghoang.sep.payment_service;

import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.runtime.StartupEvent;
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

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    void onStart(@Observes StartupEvent ev) throws InterruptedException, JsonProcessingException {
        while (true) {
            var min = 1000;
            var max = 5000;
            var delayMillis = random.nextInt(max - min + 1) + min;
            Thread.sleep(delayMillis);
            var txId = UUID.randomUUID().toString();
            var amount = delayMillis * 100; // 100k - 500k
            var voucher = random.nextBoolean() ? "SAVE50" : "FREESHIP"; // Random voucher

            var event = new PaymentInitiated(txId, "user-" + random.nextInt(100), amount, voucher);
            log.info(">> [PAYMENT] Init: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
            emitter.send(event);
        }
    }
}
