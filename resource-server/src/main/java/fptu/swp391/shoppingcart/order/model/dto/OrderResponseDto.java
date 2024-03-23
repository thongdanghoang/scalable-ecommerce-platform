package fptu.swp391.shoppingcart.order.model.dto;

import fptu.swp391.shoppingcart.cart.model.dto.CartItemResponseDto;
import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import lombok.Data;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Data
public class OrderResponseDto {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
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
