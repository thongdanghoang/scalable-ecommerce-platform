package fptu.swp391.shoppingcart.order.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import fptu.swp391.shoppingcart.order.model.dto.CheckoutInfoResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.CheckoutRequestDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderStatusDto;
import fptu.swp391.shoppingcart.order.model.exception.AddressNotFoundException;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    CheckoutInfoResponseDto retrieveCheckoutInfo();

    OrderResponseDto checkout(CheckoutRequestDto checkoutRequestDto) throws AddressNotFoundException, UnirestException, IOException;

    List<OrderResponseDto> getAllOrder();

    List<OrderResponseDto> getOrderByUserAuthenticated();

    List<OrderResponseDto> getOrderByStatus(OrderStatusDto status);

    OrderResponseDto getOrderByOrderId(Long orderId);

    OrderResponseDto paymentRequest(Long orderId) throws UnirestException, IOException;

    OrderResponseDto cancelOrder(Long orderId);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatusDto status);
}
