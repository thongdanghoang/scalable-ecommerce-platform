package fptu.swp391.shoppingcart.user.authentication.repository;

import fptu.swp391.shoppingcart.user.authentication.entity.EmailOtpEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtpEntity, Long> {

    Optional<EmailOtpEntity> findByEmailAndStatus(String email, OtpStatus status);

    Optional<EmailOtpEntity> findByToken(String token);
    Optional<EmailOtpEntity> findByEmail(String email);
}
