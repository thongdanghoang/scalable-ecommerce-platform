package fptu.swp391.shoppingcart.product.repo;

import fptu.swp391.shoppingcart.product.entity.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuantityRepository extends JpaRepository<Quantity, Long> {
}
