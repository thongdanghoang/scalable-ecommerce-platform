package fptu.swp391.shoppingcart.user.address.model.repository;

import fptu.swp391.shoppingcart.user.address.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long>{
    Optional<AddressEntity> findById(Long id);
}
