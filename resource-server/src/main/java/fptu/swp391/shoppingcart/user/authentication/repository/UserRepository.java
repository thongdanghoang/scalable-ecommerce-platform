package fptu.swp391.shoppingcart.user.authentication.repository;

import fptu.swp391.shoppingcart.admin.repository.UserRepositoryCustom;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAuthEntity, Integer>, UserRepositoryCustom {
    Optional<UserAuthEntity> findUserByUsername(String username);
    Optional<UserAuthEntity> findByProfileEmail(String email);
    Optional<UserAuthEntity> findByProfilePhone(String phone);
}
