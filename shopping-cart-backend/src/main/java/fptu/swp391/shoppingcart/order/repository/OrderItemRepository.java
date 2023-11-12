package fptu.swp391.shoppingcart.order.repository;

import fptu.swp391.shoppingcart.order.model.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
