package vn.id.thongdanghoang.n3tk.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class N3tkSecurityUtils {

    public static Set<N3tkRole> extractN3tkRolesAndValidate(Authentication identity) {
//        TODO : adapt Spring Security Resource Server getRoles and validate rold from token
//        if (!identity.getRolges().contains(N3tkRole.RoleNameConstant.BASE_ROLE)) {
//            throw new AccessDeniedException("User doesn't have base permission to access the n3tk core application.");
//        }
//        Set<N3tkRole> userRoles = identity.getRoles()
//                                              .stream()
//                                              .map(N3tkRole::fromValue)
//                                              .filter(Objects::nonNull)
//                                              .collect(Collectors.toSet());
//        if (CollectionUtils.isEmpty(userRoles)) {
//            throw new AccessDeniedException("User doesn't have digiflux role");
//        }
//        return userRoles;
        throw new AccessDeniedException("User doesn't have digiflux role");
    }
}
