package fptu.swp391.shoppingcart.order.service.impl;

import fptu.swp391.shoppingcart.cart.model.dto.CartItemResponseDto;
import fptu.swp391.shoppingcart.cart.model.dto.ClassificationDto;
import fptu.swp391.shoppingcart.cart.model.entity.Cart;
import fptu.swp391.shoppingcart.cart.model.entity.CartItem;
import fptu.swp391.shoppingcart.cart.model.exception.OutOfStockException;
import fptu.swp391.shoppingcart.cart.repository.CartRepository;
import fptu.swp391.shoppingcart.order.model.dto.*;
import fptu.swp391.shoppingcart.order.model.entity.OrderEntity;
import fptu.swp391.shoppingcart.order.model.entity.OrderItemEntity;
import fptu.swp391.shoppingcart.order.model.entity.PaymentDetail;
import fptu.swp391.shoppingcart.order.model.entity.enums.DeliveryMethod;
import fptu.swp391.shoppingcart.order.model.entity.enums.OrderStatus;
import fptu.swp391.shoppingcart.order.model.entity.enums.PaymentMethod;
import fptu.swp391.shoppingcart.order.model.entity.enums.PaymentStatus;
import fptu.swp391.shoppingcart.order.model.exception.AddressNotFoundException;
import fptu.swp391.shoppingcart.order.model.exception.CartEmptyException;
import fptu.swp391.shoppingcart.order.model.exception.OrderNotFoundException;
import fptu.swp391.shoppingcart.order.model.exception.PaymentPendingException;
import fptu.swp391.shoppingcart.order.model.mapping.CheckoutInfoResponseMapper;
import fptu.swp391.shoppingcart.order.repository.OrderItemRepository;
import fptu.swp391.shoppingcart.order.repository.OrderRepository;
import fptu.swp391.shoppingcart.order.service.OrderService;
import fptu.swp391.shoppingcart.product.repo.QuantityRepository;
import fptu.swp391.shoppingcart.user.address.model.entity.AddressEntity;
import fptu.swp391.shoppingcart.user.address.model.mapping.AddressMapper;
import fptu.swp391.shoppingcart.user.address.model.repository.AddressRepository;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final CheckoutInfoResponseMapper checkoutInfoResponseMapper;

    private final AddressMapper addressMapper;
    private final QuantityRepository quantityRepository;

    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserRepository userRepository,
                            CartRepository cartRepository,
                            CheckoutInfoResponseMapper checkoutInfoResponseMapper,
                            AddressMapper addressMapper,
                            QuantityRepository quantityRepository,
                            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.checkoutInfoResponseMapper = checkoutInfoResponseMapper;
        this.addressMapper = addressMapper;
        this.quantityRepository = quantityRepository;
        this.addressRepository = addressRepository;
    }


    @Override
    public CheckoutInfoResponseDto retrieveCheckoutInfo() {
        Cart cart = cartRepository.findByUserAuthEntityUsername(getAuthenticatedUser().getUsername()).orElseThrow();
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Your cart is empty");
        }
        Set<CartItem> items = cart.getItems();
        checkCartItemsQuantityToProductQuantityInStock(items, cart);
        var order = new OrderEntity();
        order.setOrderItems(mapCartItemsToOrderItems(items));

        setOrderItemsFromCart(order, items);
        calculateOrderDiscount(order, items);

        order.setGrandTotal(order.getTotal() - order.getDiscount() + order.getShippingFee());
        order.setShippingFee(order.getTotal() > 30000 ? 0 : 30000);
        return checkoutInfoResponseMapper.toDTO(order);
    }

    @Override
    public OrderResponseDto checkout(CheckoutRequestDto checkoutRequestDto) throws AddressNotFoundException {
        var user = getAuthenticatedUser();
        Cart cart = cartRepository.findByUserAuthEntityUsername(user.getUsername()).orElseThrow();
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Your cart is empty");
        }
        Set<CartItem> items = cart.getItems();
        checkCartItemsQuantityToProductQuantityInStock(items, cart);
        var order = new OrderEntity();

        order.setUser(user);
        Optional<AddressEntity> address = addressRepository.findById(checkoutRequestDto.getAddressId());
        if(address.isEmpty()){
            throw new AddressNotFoundException(String.format("Address with id %d not found", checkoutRequestDto.getAddressId()));
        }
        order.setAddressEntity(address.get());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryMethod(DeliveryMethod.valueOf(checkoutRequestDto.getDeliveryMethodDto().toString()));
        order.setOrderItems(new HashSet<>(orderItemRepository.saveAll(mapCartItemsToOrderItems(items))));

        setOrderItemsFromCart(order, items);
        calculateOrderDiscount(order, items);

        order.setGrandTotal(order.getTotal() - order.getDiscount() + order.getShippingFee());
        order.setShippingFee(order.getTotal() > 30000 ? 0 : 30000);

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentMethod(PaymentMethod.valueOf(checkoutRequestDto.getPaymentMethod().toString()));
        paymentDetail.setGrandTotal(order.getGrandTotal());
        paymentDetail.setPaymentStatus(PaymentStatus.PENDING);
        paymentDetail.setOrderEntity(order);
        order.setPaymentDetail(paymentDetail);

        OrderEntity saved = orderRepository.save(order);
        // clear cart
        saved.getOrderItems().forEach(orderItemEntity ->
                quantityRepository
                        .findById(orderItemEntity.getQuantity().getId())
                        .ifPresent(quantityEntity -> {
                            quantityEntity
                                    .setQuantityInStock(quantityEntity.getQuantityInStock() - orderItemEntity.getAmount());
                            quantityRepository.save(quantityEntity);
                        }));
        cart.getItems().clear();
        cartRepository.save(cart);
        OrderResponseDto checkoutResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(saved, checkoutResponseDto);
        return checkoutResponseDto;
    }


    @Override
    public List<OrderResponseDto> getAllOrder() {
        var orders = orderRepository.findAll();
        List<OrderResponseDto> ordersResponse = new ArrayList<>();
        orders.forEach(saved -> {
            var checkoutResponseDto = new OrderResponseDto();
            mapOrderEntityToOrderResponseDto(saved, checkoutResponseDto);
            ordersResponse.add(checkoutResponseDto);
        });
        return ordersResponse;
    }

    @Override
    public List<OrderResponseDto> getOrderByUserAuthenticated() {
        var orders = orderRepository.findAllByUserUsername(getAuthenticatedUser().getUsername());
        List<OrderResponseDto> ordersResponse = new ArrayList<>();
        orders.forEach(saved -> {
            var checkoutResponseDto = new OrderResponseDto();
            mapOrderEntityToOrderResponseDto(saved, checkoutResponseDto);
            ordersResponse.add(checkoutResponseDto);
        });
        return ordersResponse;
    }


    @Override
    public List<OrderResponseDto> getOrderByStatus(OrderStatusDto status) {
        var orders = orderRepository.findAllByStatus(OrderStatus.valueOf(status.toString()));
        UserAuthEntity user = getAuthenticatedUser();
        if (user.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getName().equals("ROLE_USER"))) {
            orders = orderRepository.findAllByUserUsernameAndStatus(user.getUsername(), OrderStatus.valueOf(status.toString()));
        }
        List<OrderResponseDto> ordersResponse = new ArrayList<>();
        orders.forEach(saved -> {
            var checkoutResponseDto = new OrderResponseDto();
            mapOrderEntityToOrderResponseDto(saved, checkoutResponseDto);
            ordersResponse.add(checkoutResponseDto);
        });
        return ordersResponse;
    }

    @Override
    public OrderResponseDto getOrderByOrderId(Long orderId) {
        var found = orderRepository.findById(orderId);
        UserAuthEntity user = getAuthenticatedUser();
        if (user.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getName().equals("ROLE_USER"))
                && (orderRepository.findByIdAndUserUsername(orderId, user.getUsername()).isEmpty())) {
            throw new AuthorizationException("You are not authorized to access this resource");
        }
        if (found.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        OrderEntity order = found.get();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(order, orderResponseDto);
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto paymentRequest(Long orderId) {
        Optional<OrderEntity> found = orderRepository.findById(orderId);
        if (found.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        var order = found.get();
        PaymentDetail paymentDetail = order.getPaymentDetail();
        if (!paymentDetail.getPaymentStatus().equals(PaymentStatus.PENDING)) {
            throw new PaymentPendingException(String.format("Order with id %d is not valid to payment", orderId));
        }
        if (paymentDetail.getPaymentMethod().equals(PaymentMethod.MOMO) ||
                paymentDetail.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            paymentDetail.setPaymentStatus(PaymentStatus.PAID);
        } else {
            throw new UnsupportedOperationException(String.format("Payment method %s is not supported yet",
                    paymentDetail.getPaymentMethod()));
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(orderRepository.save(order), orderResponseDto);
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId) {
        Optional<OrderEntity> found = orderRepository.findById(orderId);
        if (found.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        var order = found.get();
        if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PROCESSING)) {
            order.setStatus(OrderStatus.CANCELLED);
            order.getOrderItems().forEach(orderItemEntity ->
                    quantityRepository
                            .findById(orderItemEntity.getQuantity().getId())
                            .ifPresent(quantityEntity -> {
                                quantityEntity
                                        .setQuantityInStock(quantityEntity.getQuantityInStock() + orderItemEntity.getAmount());
                                quantityRepository.save(quantityEntity);
                            }));
        } else {
            throw new UnsupportedOperationException(String.format("Order with id %d is not valid to cancel", orderId));
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(orderRepository.save(order), orderResponseDto);
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusDto status) {
        Optional<OrderEntity> found = orderRepository.findById(orderId);
        if (found.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        var order = found.get();
        order.setStatus(OrderStatus.valueOf(status.toString()));
        if (order.getStatus().equals(OrderStatus.CANCELLED) || order.getStatus().equals(OrderStatus.RETURNED)) {
            // reset quantity in stock
            order.getOrderItems().forEach(orderItemEntity ->
                    quantityRepository
                            .findById(orderItemEntity.getQuantity().getId())
                            .ifPresent(quantityEntity -> {
                                quantityEntity
                                        .setQuantityInStock(quantityEntity.getQuantityInStock() + orderItemEntity.getAmount());
                                quantityRepository.save(quantityEntity);
                            }));
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(orderRepository.save(order), orderResponseDto);
        return orderResponseDto;
    }

    private Set<OrderItemEntity> mapCartItemsToOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream().map(this::mapCartItemToOrderItem).collect(Collectors.toSet());
    }

    private OrderItemEntity mapCartItemToOrderItem(CartItem cartItem) {
        var orderItem = new OrderItemEntity();
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setAmount(cartItem.getAmount());
        return orderItem;
    }

    private UserAuthEntity getAuthenticatedUser() {
        return userRepository
                .findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow();
    }

    private void mapOrderEntityToOrderResponseDto(OrderEntity entity, OrderResponseDto dto) {
        dto.setOrderId(entity.getId());
        dto.setStatus(entity.getStatus().toString());
        dto.setAddress(addressMapper.toDTO(entity.getAddressEntity()));
        dto.setDeliveryMethod(DeliveryMethodDto.valueOf(entity.getDeliveryMethod().toString()));
        dto.setPaymentMethod(PaymentMethodDto.valueOf(entity.getPaymentDetail().getPaymentMethod().toString()));
        dto.setTotal(entity.getTotal());
        dto.setShoppingFee(entity.getShippingFee());
        dto.setDiscount(entity.getDiscount());
        dto.setGrandTotal(entity.getGrandTotal());
        dto.setPaymentStatus(entity.getPaymentDetail().getPaymentStatus().toString());
        dto.setCreatedAt(entity.getCreatedDate());
        dto.setItems(checkoutInfoResponseMapper.toDTO(entity).getItems());
    }

    private void checkCartItemsQuantityToProductQuantityInStock(Set<CartItem> items, Cart cart) {
        items.forEach(cartItem -> {
            if (cartItem.getQuantity().getQuantityInStock() < cartItem.getAmount()) {
                cart.getItems().remove(cartItem);
                cartRepository.save(cart);
                throw new OutOfStockException(String.format("Product %s is out of stock. We already remove it from your cart. Sorry for this inconvenience.",
                        cartItem.getQuantity().getProduct().getName()));
            }
        });
    }

    private void setOrderItemsFromCart(OrderEntity order, Set<CartItem> items) {
        order.setTotal(items
                .stream()
                .mapToLong(value -> (long) value.getAmount() * value.getQuantity().getProduct().getPrice())
                .sum());
    }

    private void calculateOrderDiscount(OrderEntity order, Set<CartItem> items) {
        order.setDiscount(order.getTotal() - items
                .stream()
                .mapToLong(value -> value.getAmount() * value.getQuantity().getProduct().getGrandTotal())
                .sum());
    }
}