package fptu.swp391.shoppingcart.admin.repository;

import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    List<UserAuthEntity> findAllByAuthoritiesNotContainingRoleUser();

    Optional<UserAuthEntity> findByAuthoritiesNotContainingRoleUserAndUsername(String username);
}
