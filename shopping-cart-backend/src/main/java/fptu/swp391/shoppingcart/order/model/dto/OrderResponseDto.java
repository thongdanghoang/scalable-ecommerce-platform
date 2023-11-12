package fptu.swp391.shoppingcart.order.model.dto;

import fptu.swp391.shoppingcart.cart.model.dto.CartItemResponseDto;
import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponseDto {
    private long orderId;
    private String status;
    private AddressDto address;
    private PaymentMethodDto paymentMethod;
    private Set<CartItemResponseDto> items;
    private String paymentStatus;
    private DeliveryMethodDto deliveryMethod;
    private long total;
    private long shoppingFee;
    private double discount;
    private long grandTotal;
    private LocalDateTime createdAt;
}
