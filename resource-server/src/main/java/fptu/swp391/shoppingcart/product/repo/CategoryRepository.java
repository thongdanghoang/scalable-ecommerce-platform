package fptu.swp391.shoppingcart.product.repo;

import fptu.swp391.shoppingcart.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
