package vn.id.thongdanghoang.payment_service.api.rest;

import vn.id.thongdanghoang.payment_service.TransactionRequest;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Path("/payment")
@RequiredArgsConstructor
public class PaymentResource {

    @Channel("words-out")
    Emitter<String> emitter;

    @POST
    @Path("/initiate-transaction")
    public String initiate(TransactionRequest req) {
        Stream.of("Hello", "with", "Quarkus", "Messaging", "message").forEach(string -> emitter.send(string));
        boolean success = Math.random() > 0.2; // Simulate 80% success
        String event = "{\"transactionId\": \"" + req.id() + "\", \"status\": \"" + (success ? "SUCCESS" : "FAILURE") + "\"}";
        emitter.send(event);
        return success ? "Success" : "Failure";
    }
}
