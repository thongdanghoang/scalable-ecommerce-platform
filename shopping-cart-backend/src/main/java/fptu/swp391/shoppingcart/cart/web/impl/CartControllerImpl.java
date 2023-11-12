package fptu.swp391.shoppingcart.cart.web.impl;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.cart.model.dto.CartDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartItemDto;
import fptu.swp391.shoppingcart.cart.model.dto.CartResponseDto;
import fptu.swp391.shoppingcart.cart.model.exception.CartItemException;
import fptu.swp391.shoppingcart.cart.model.exception.OutOfStockException;
import fptu.swp391.shoppingcart.cart.service.CartService;
import fptu.swp391.shoppingcart.cart.web.CartController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl extends AbstractApplicationController implements CartController {

    private final Logger logger = LogManager.getLogger(CartControllerImpl.class);

    @Autowired
    private CartService cartService;

    @PostMapping
    @Override
    public ResponseEntity<CartResponseDto> addToCart(CartItemDto cartItemDto) {
        try {
            return ResponseEntity.ok(cartService.addToCart(cartItemDto));
        } catch (OutOfStockException | CartItemException | NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping
    @Override
    public ResponseEntity<CartResponseDto> updateCart(CartDto cartDto) {
        try {
            return ResponseEntity.ok(cartService.modifyCart(cartDto));
        } catch (OutOfStockException | CartItemException | NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<CartResponseDto> viewCart() {
        return ResponseEntity.ok(cartService.viewCart());
    }

    @DeleteMapping("/{quantityId}")
    @Override
    public ResponseEntity<CartResponseDto> deleteCartItem(Long quantityId) {
        return ResponseEntity.ok(cartService.deleteCartItem(quantityId));
    }
}
