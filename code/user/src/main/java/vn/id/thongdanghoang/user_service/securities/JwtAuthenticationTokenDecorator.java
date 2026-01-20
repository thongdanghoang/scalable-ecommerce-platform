package vn.id.thongdanghoang.user_service.securities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import lombok.Getter;

@Getter
public class JwtAuthenticationTokenDecorator extends JwtAuthenticationToken {

    private final UserContextData principal;

    public JwtAuthenticationTokenDecorator(Jwt jwt, UserContextData principal) {
        super(jwt, principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return principal.getAuthorities();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
