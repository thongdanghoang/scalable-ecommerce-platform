package vn.id.thongdanghoang.user_service.securities;

import vn.id.thongdanghoang.user_service.entities.User_;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;
import vn.id.thongdanghoang.user_service.utils.JpaUtils;

import java.util.Objects;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JwtAuthenticationTokenConverter implements Converter<Jwt, JwtAuthenticationTokenDecorator> {

    private final UserRepository userRepository;

    public JwtAuthenticationTokenConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public JwtAuthenticationTokenDecorator convert(Jwt source) {
        var sub = Objects.requireNonNull(source.getClaims().get("sub"), "JWT 'sub' claim is required");
        var userId = UUID.fromString(sub.toString());
        var retrievedUser = userRepository
                .findById(userId)
                .map(user -> JpaUtils.initialize(user, User_.AUTHORITIES))
                .orElseThrow();
        var authorities = retrievedUser.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .map(GrantedAuthority.class::cast)
                .toList();
        return new JwtAuthenticationTokenDecorator(source,
                UserContextData.builder().user(retrievedUser).authorities(authorities).build());
    }

}
