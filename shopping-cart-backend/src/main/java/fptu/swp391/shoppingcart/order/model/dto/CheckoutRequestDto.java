package fptu.swp391.shoppingcart.order.model.dto;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private long addressId;
    private PaymentMethodDto paymentMethod;
    private DeliveryMethodDto deliveryMethodDto;
}
