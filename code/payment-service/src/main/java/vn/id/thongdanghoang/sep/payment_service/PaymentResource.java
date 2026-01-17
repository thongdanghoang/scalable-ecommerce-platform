package vn.id.thongdanghoang.sep.payment_service;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/payments")
public class PaymentResource {

    @POST
    @Path("/initiate")
    public PromotionApplied initiatePayment(PaymentInitiated payload) {
        return null;
    }
}
