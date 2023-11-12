package fptu.swp391.shoppingcart.cart.service;

import fptu.swp391.shoppingcart.cart.model.dto.CartDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartItemDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartResponseDto;

public interface CartService {
    CartResponseDto viewCart();

    CartResponseDto addToCart(CartItemDto cartItemDto);

    CartResponseDto modifyCart(CartDto cartDto);

    CartResponseDto deleteCartItem(Long quantityId);
}
