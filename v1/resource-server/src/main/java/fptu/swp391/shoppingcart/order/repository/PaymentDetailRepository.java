package fptu.swp391.shoppingcart.order.repository;

import fptu.swp391.shoppingcart.order.model.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
