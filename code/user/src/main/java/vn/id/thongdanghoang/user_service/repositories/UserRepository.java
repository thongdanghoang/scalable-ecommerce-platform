package vn.id.thongdanghoang.user_service.repositories;

import vn.id.thongdanghoang.user_service.entities.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByProviderId(String providerId);

}
