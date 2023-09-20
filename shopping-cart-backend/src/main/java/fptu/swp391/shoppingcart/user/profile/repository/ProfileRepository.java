package fptu.swp391.shoppingcart.user.profile.repository;

import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUserUsername(String username);

    Optional<ProfileEntity> findById(Long id);

    boolean existsByEmail(String email);
}
