package fptu.swp391.shoppingcart.user.authentication.repository;

import fptu.swp391.shoppingcart.user.authentication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByUsername(String username);
}
