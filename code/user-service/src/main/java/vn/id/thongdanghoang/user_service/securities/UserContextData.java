package vn.id.thongdanghoang.user_service.securities;

import vn.id.thongdanghoang.user_service.entities.User;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserContextData implements UserDetails {

    private final List<GrantedAuthority> authorities;

    /**
     * Deprecated: Not used in JWT-based authentication. The UserDetails contract requires these fields,
     * but JWT tokens provide authentication without username/password credentials.
     */
    @Deprecated
    private final String password;

    private final User user;

    /**
     * Deprecated: Not used in JWT-based authentication. The UserDetails contract requires these fields,
     * but JWT tokens provide authentication without username/password credentials.
     */
    @Deprecated
    private final String username;

    @Override
    public boolean isEnabled() {
        return !user.isDisabled();
    }
}
