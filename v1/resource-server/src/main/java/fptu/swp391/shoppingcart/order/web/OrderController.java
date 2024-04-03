package fptu.swp391.shoppingcart.order.web;

import fptu.swp391.shoppingcart.order.model.dto.CheckoutInfoResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.CheckoutRequestDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderStatusDto;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {
    // ✅
    ResponseEntity<CheckoutInfoResponseDto> retrieveCheckoutInfo();

    // ✅
    ResponseEntity<OrderResponseDto> checkout(CheckoutRequestDto checkoutRequestDto);

    // TODO : adjust response to specific whose own the order
    // TODO : just admin just this api
    ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrder();

    // TODO : user use this api
    ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderByUserAuthenticated();

    // TODO : admin use this api or user whose own the order
    ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderByStatus(OrderStatusDto status);

    // TODO : admin use this api or user whose own the order
    ResponseEntity<ApiResponse<OrderResponseDto>> getOrderByOrderId(Long orderId);


    // TODO : check user is owner of order
    ResponseEntity<ApiResponse<OrderResponseDto>> paymentRequest(Long orderId);

    ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(Long orderId);

    ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(Long orderId, OrderStatusDto status);
}
