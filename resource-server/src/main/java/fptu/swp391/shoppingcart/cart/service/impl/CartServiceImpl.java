package fptu.swp391.shoppingcart.cart.service.impl;

import fptu.swp391.shoppingcart.cart.model.dto.CartDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartItemDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartResponseDto;
import fptu.swp391.shoppingcart.cart.model.entity.Cart;
import fptu.swp391.shoppingcart.cart.model.entity.CartItem;
import fptu.swp391.shoppingcart.cart.model.exception.CartItemException;
import fptu.swp391.shoppingcart.cart.model.exception.OutOfStockException;
import fptu.swp391.shoppingcart.cart.model.mapping.CartItemResponseMapper;
import fptu.swp391.shoppingcart.cart.repository.CartRepository;
import fptu.swp391.shoppingcart.cart.service.CartService;
import fptu.swp391.shoppingcart.product.entity.Quantity;
import fptu.swp391.shoppingcart.product.repo.QuantityRepository;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final QuantityRepository quantityRepository;

    private final CartItemResponseMapper cartItemMapper;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, QuantityRepository quantityRepository, CartItemResponseMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.quantityRepository = quantityRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public CartResponseDto viewCart() {
        Cart cart = getUserCartFromDB();
        cart.getItems().forEach(item -> {
            var inStock = quantityRepository.findById(item.getQuantity().getId()).orElseThrow();
            if (inStock.getQuantityInStock() == 0) {
                cart.getItems().remove(item);
                cartRepository.save(cart);
                throw new OutOfStockException(String.format("Product %s was out of stock", inStock.getProduct().getName()));
            }
            if (item.getAmount() > inStock.getQuantityInStock()) {
                item.setAmount(inStock.getQuantityInStock());
                cartRepository.save(cart);
                throw new OutOfStockException(String.format("Product %s was out of stock, we have updated the amount to %d", inStock.getProduct().getName(), inStock.getQuantityInStock()));
            }
        });
        return mapCartToCartResponseDto(cart);
    }

    @Override
    public CartResponseDto addToCart(CartItemDto cartItemDto) {

        if (cartItemDto.getAmount() == 0) {
            throw new CartItemException("Amount must be greater than 0");
        }

        CartItem cartItem = new CartItem();
        cartItem.setAmount(cartItemDto.getAmount());
        Quantity inStock = quantityRepository.findById(cartItemDto.getQuantityId()).orElseThrow();
        cartItem.setQuantity(inStock);

        var userCart = getUserCartFromDB();
        if (userCart.getItems().contains(cartItem)) {
            // increase amount if item already exists
            userCart.getItems()
                    .stream()
                    .filter(item -> item.equals(cartItem))
                    .findFirst()
                    .ifPresent(item -> item.setAmount(item.getAmount() + cartItem.getAmount()));
        } else {
            userCart.getItems().add(cartItem);
        }

        if (cartItem.getAmount() > inStock.getQuantityInStock()) {
            throw new OutOfStockException(String.format("Product %s was out of stock", inStock.getProduct().getName()));
        }

        return mapCartToCartResponseDto(cartRepository.save(userCart));
    }

    @Override
    public CartResponseDto modifyCart(CartDto cartDto) {
        Cart userCart = getUserCartFromDB();
        Set<CartItem> entityItems = userCart.getItems();
        Set<CartItemDto> items = cartDto.getItems();
        items.forEach(item -> {
            Quantity inStock = quantityRepository.findById(item.getQuantityId()).orElseThrow();
            if (item.getAmount() < 1) {
                throw new CartItemException("Amount must be greater than 0");
            }
            if (item.getAmount() > inStock.getQuantityInStock()) {
                throw new OutOfStockException(String.format("Product %s was out of stock", inStock.getProduct().getName()));
            }
            Optional<CartItem> found = entityItems.stream()
                    .filter(cartItem -> cartItem.getQuantity().getId().equals(item.getQuantityId()))
                    .findFirst();
            if (found.isPresent()) { // if exists, update amount
                found.get().setAmount(item.getAmount());
            } else { // if not exists, add new item
                CartItem cartItem = new CartItem();
                cartItem.setAmount(item.getAmount());
                cartItem.setQuantity(inStock);
                entityItems.add(cartItem);
            }
        });
        return mapCartToCartResponseDto(cartRepository.save(userCart));
    }

    @Override
    public CartResponseDto deleteCartItem(Long quantityId) {
        var userCart = getUserCartFromDB();
        userCart.getItems().removeIf(item -> item.getQuantity().getId().equals(quantityId));
        return mapCartToCartResponseDto(cartRepository.save(userCart));
    }

    @Override
    public void clearCart() {
        var userCart = getUserCartFromDB();
        userCart.getItems().clear();
        cartRepository.save(userCart);
    }

    private Cart getUserCartFromDB() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var cart = cartRepository.findByUserAuthEntityUsername(username);
        if (cart.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setItems(new HashSet<>());
            newCart.setUserAuthEntity(userRepository.findUserByUsername(username).orElseThrow());
            return cartRepository.save(newCart);
        }
        return cart.get();
    }

    private CartResponseDto mapCartToCartResponseDto(Cart cart) {
        return new CartResponseDto(cart.getItems().stream().map(cartItemMapper::toDTO).collect(Collectors.toSet()));
    }
}
