package vn.id.thongdanghoang.service.user;

import vn.id.thongdanghoang.service.user.entities.User;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByProviderLinksProviderId(String providerId);

}
