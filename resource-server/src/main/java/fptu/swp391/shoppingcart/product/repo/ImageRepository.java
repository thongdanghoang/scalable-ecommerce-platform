package fptu.swp391.shoppingcart.product.repo;

import fptu.swp391.shoppingcart.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByProductIdAndColorIdAndUrl(Long productId, Long colorId, String url);

    void deleteByProductId(Long productId);
}
