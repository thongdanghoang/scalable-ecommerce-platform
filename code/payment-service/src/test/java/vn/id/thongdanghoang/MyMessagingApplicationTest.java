package vn.id.thongdanghoang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import vn.id.thongdanghoang.payment_service.api.kafka.PaymentIncoming;

import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MyMessagingApplicationTest {

    @Inject
    PaymentIncoming application;

    @Test
    void test() {
        assertEquals("HELLO", application.toUpperCase(Message.of("Hello")).getPayload());
        assertEquals("BONJOUR", application.toUpperCase(Message.of("bonjour")).getPayload());
    }
}
