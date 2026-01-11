package vn.id.thongdanghoang.user_service.repositories;

import vn.id.thongdanghoang.user_service.entities.UserProfile;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserId(UUID userId);
}
