package fptu.swp391.shoppingcart.user.otp.repo;

import fptu.swp391.shoppingcart.user.otp.entities.EmailOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtpEntity, Long> {

    Optional<EmailOtpEntity> findByEmail(String email);
}
