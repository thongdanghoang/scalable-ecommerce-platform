package fptu.swp391.shoppingcart.product.repo;

import fptu.swp391.shoppingcart.product.entity.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuantityRepository extends JpaRepository<Quantity, Long> {
    Optional<Quantity> findByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId);

    void deleteByProductId(Long productId);
}
