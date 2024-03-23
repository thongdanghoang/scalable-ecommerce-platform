package fptu.swp391.shoppingcart.order.web.impl;

import com.mashape.unirest.http.exceptions.UnirestException;
import fptu.swp391.shoppingcart.cart.model.exception.OutOfStockException;
import fptu.swp391.shoppingcart.order.model.dto.CheckoutInfoResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.CheckoutRequestDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderResponseDto;
import fptu.swp391.shoppingcart.order.model.dto.OrderStatusDto;
import fptu.swp391.shoppingcart.order.model.exception.AddressNotFoundException;
import fptu.swp391.shoppingcart.order.model.exception.CartEmptyException;
import fptu.swp391.shoppingcart.order.model.exception.OrderNotFoundException;
import fptu.swp391.shoppingcart.order.model.exception.PaymentPendingException;
import fptu.swp391.shoppingcart.order.service.OrderService;
import fptu.swp391.shoppingcart.order.web.OrderController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    public OrderControllerImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/checkout-info") // role user
    @Override
    public ResponseEntity<CheckoutInfoResponseDto> retrieveCheckoutInfo() {
        try {
            return ResponseEntity.ok(orderService.retrieveCheckoutInfo());
        } catch (OutOfStockException | CartEmptyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/checkout") // role user
    @Override
    public ResponseEntity<OrderResponseDto> checkout(@RequestBody CheckoutRequestDto checkoutRequestDto) {
        try {
            return ResponseEntity.ok(orderService.checkout(checkoutRequestDto));
        } catch (OutOfStockException | CartEmptyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AddressNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnirestException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @Override
    @GetMapping("/user") // role user
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderByUserAuthenticated() {
        return ResponseEntity.ok(new ApiResponse<>("Get user's orders success", true, orderService.getOrderByUserAuthenticated()));
    }

    @Override
    @GetMapping("/all") // role shop owner
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrder() {
        return ResponseEntity.ok(new ApiResponse<>("Get all orders success", true, orderService.getAllOrder()));
    }

    @Override
    @GetMapping("/status/{status}")  // role shop owner && role user
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderByStatus(@PathVariable OrderStatusDto status) {
        return ResponseEntity.ok(new ApiResponse<>("Get an order success", true, orderService.getOrderByStatus(status)));
    }

    @Override
    @GetMapping("/{orderId}") // role shop owner && role user
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderByOrderId(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Get order by id success", true, orderService.getOrderByOrderId(orderId)));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @Override
    @GetMapping("/payment/{orderId}") // role user
    public ResponseEntity<ApiResponse<OrderResponseDto>> paymentRequest(@PathVariable Long orderId) {
        String message = "Because we haven't develop payment gateway yet, " +
                "so we just assume that payment request is success with MOMO or Credit Card.";
        try {
            return ResponseEntity.ok(new ApiResponse<>(message, true, orderService.paymentRequest(orderId)));
        } catch (PaymentPendingException | OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, e.getMessage());
        } catch (UnirestException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}") // role user
    @Override
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Cancel order success", true, orderService.cancelOrder(orderId)));
        } catch (OrderNotFoundException | UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orderId}") // role shop owner
    @Override
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatusDto status) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("Update order status success", true, orderService.updateOrderStatus(orderId, status)));
        } catch (OrderNotFoundException | UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
