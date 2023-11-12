package fptu.swp391.shoppingcart.cart.repository;

import fptu.swp391.shoppingcart.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAuthEntityUsername(String username);
}
