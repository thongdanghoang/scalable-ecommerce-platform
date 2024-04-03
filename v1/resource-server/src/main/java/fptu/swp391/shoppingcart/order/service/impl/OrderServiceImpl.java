package fptu.swp391.shoppingcart.order.service.impl;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fptu.swp391.shoppingcart.cart.model.entity.Cart;
import fptu.swp391.shoppingcart.cart.model.entity.CartItem;
import fptu.swp391.shoppingcart.cart.model.exception.OutOfStockException;
import fptu.swp391.shoppingcart.cart.repository.CartRepository;
import fptu.swp391.shoppingcart.order.model.dto.*;
import fptu.swp391.shoppingcart.order.model.entity.OrderAddress;
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
import fptu.swp391.shoppingcart.product.repo.ProductRepository;
import fptu.swp391.shoppingcart.product.repo.QuantityRepository;
import fptu.swp391.shoppingcart.user.address.model.entity.AddressEntity;
import fptu.swp391.shoppingcart.user.address.model.mapping.AddressMapper;
import fptu.swp391.shoppingcart.user.address.model.repository.AddressRepository;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CheckoutInfoResponseMapper checkoutInfoResponseMapper;
    private final AddressMapper addressMapper;
    private final QuantityRepository quantityRepository;
    private final AddressRepository addressRepository;

    @Value("${mailgun.domain.name}")
    private String mailgunDomainName;
    @Value("${mailgun.api.key}")
    private String apiKey;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository,
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
        this.productRepository = productRepository;
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
    public OrderResponseDto checkout(CheckoutRequestDto checkoutRequestDto) throws AddressNotFoundException, UnirestException, IOException {
        var user = getAuthenticatedUser();
        var cart = cartRepository.findByUserAuthEntityUsername(user.getUsername()).orElseThrow();
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Your cart is empty");
        }
        var items = cart.getItems();
        checkCartItemsQuantityToProductQuantityInStock(items, cart);
        var order = new OrderEntity();

        order.setUser(user);
        var address = addressRepository.findById(checkoutRequestDto.getAddressId());
        if (address.isEmpty()) {
            throw new AddressNotFoundException(String.format("Address with id %d not found", checkoutRequestDto.getAddressId()));
        }
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setFullName(address.get().getFullName());
        orderAddress.setPhone(address.get().getPhone());
        orderAddress.setDistrict(address.get().getDistrict());
        orderAddress.setProvince(address.get().getProvince());
        orderAddress.setWard(address.get().getWard());
        orderAddress.setAddressDetail(address.get().getAddressDetail());
        orderAddress.setType(orderAddress.getType());

        order.setOrderAddress(orderAddress);
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
        sendOrderConfirmationEmail(checkoutResponseDto, user.getProfile().getEmail());
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
    public OrderResponseDto paymentRequest(Long orderId) throws UnirestException, IOException {
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
        } else if (paymentDetail.getPaymentMethod().equals(PaymentMethod.CASH_ON_DELIVERY)) {
            paymentDetail.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            throw new UnsupportedOperationException(String.format("Payment method %s is not supported yet",
                    paymentDetail.getPaymentMethod()));
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(orderRepository.save(order), orderResponseDto);
        sendOrderConfirmationEmail(orderResponseDto, order.getUser().getProfile().getEmail());
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
        if (order.getStatus().equals(OrderStatus.COMPLETED)) {
            for (OrderItemEntity item : order.getOrderItems()) {
                var product = item.getQuantity().getProduct();
                product.setNumberOfSold(product.getNumberOfSold() + item.getAmount());
                productRepository.save(product);
            }
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        mapOrderEntityToOrderResponseDto(orderRepository.save(order), orderResponseDto);
        return orderResponseDto;
    }

    private void sendOrderConfirmationEmail(OrderResponseDto orderResponseDto, String target) throws UnirestException, IOException {
        // read order template from src/main/resources
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("order.html");
        String orderTemplate;

        orderTemplate = readInputStreamToString(inputStream);

        // order id which format #000000
        orderTemplate = orderTemplate.replace("${orderResponseDto.orderId}", String.format("#%06d", orderResponseDto.getOrderId()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.status}", formatStatus(orderResponseDto.getStatus()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.address}", orderResponseDto.getAddress().toString());
        orderTemplate = orderTemplate.replace("${orderResponseDto.paymentMethod}", orderResponseDto.getPaymentMethod().getLable());
        orderTemplate = orderTemplate.replace("${orderResponseDto.paymentStatus}", formatStatus(orderResponseDto.getPaymentStatus()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.deliveryMethod}", orderResponseDto.getDeliveryMethod().getLabel());
        orderTemplate = orderTemplate.replace("${orderResponseDto.total}", OrderResponseDto.NUMBER_FORMAT.format(orderResponseDto.getTotal()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.shoppingFee}", OrderResponseDto.NUMBER_FORMAT.format(orderResponseDto.getShoppingFee()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.discount}", OrderResponseDto.NUMBER_FORMAT.format(orderResponseDto.getDiscount()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.grandTotal}", OrderResponseDto.NUMBER_FORMAT.format(orderResponseDto.getGrandTotal()));
        orderTemplate = orderTemplate.replace("${orderResponseDto.createdAt}", orderResponseDto.getCreatedAt().format(OrderResponseDto.DATE_TIME_FORMATTER));

        Unirest.post("https://api.mailgun.net/v3/"
                        + mailgunDomainName + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", "N3TK Shop<noreply@thongdanghoang.id.vn>")
                .queryString("to", target)
                .queryString("subject", "Your order confirmation")
                .queryString("html", orderTemplate)
                .asJson();
    }

    private String formatStatus(String status) {
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
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

        OrderAddress orderAddress = entity.getOrderAddress();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFullName(orderAddress.getFullName());
        addressEntity.setPhone(orderAddress.getPhone());
        addressEntity.setDistrict(orderAddress.getDistrict());
        addressEntity.setProvince(orderAddress.getProvince());
        addressEntity.setWard(orderAddress.getWard());
        addressEntity.setAddressDetail(orderAddress.getAddressDetail());
        dto.setAddress(addressMapper.toDTO(addressEntity));

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

    private String readInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}