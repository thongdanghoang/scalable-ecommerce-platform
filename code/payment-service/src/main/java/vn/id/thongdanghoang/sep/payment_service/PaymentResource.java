package vn.id.thongdanghoang.sep.payment_service;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/payments")
@Slf4j
public class PaymentResource {

    @POST
    @Path("/initiate")
    public PromotionApplied initiatePayment(PaymentInitiated payload) {
        return null;
    }

    @POST
    public Response enqueueMovie(Movie movie) {
        log.info("Sending movie %s to Kafka", movie.getTitle());
        return Response.accepted().build();
    }
}
