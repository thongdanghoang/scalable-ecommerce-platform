package fptu.swp391.shoppingcart.user.authentication.service;

import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class GoogleAuthtication implements Authentication {

    private String username;
    private String principal;
    private UserAuthEntity userDetail;
    private boolean isAuthenticated;

    public GoogleAuthtication(String username, String principal, boolean isAuthenticated, UserAuthEntity userDetail) {
        this.username = username;
        this.principal = principal;
        this.isAuthenticated = isAuthenticated;
        // cast set of authorities to collection of authorities
        this.userDetail = userDetail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetail.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return userDetail.getPassword();
    }

    @Override
    public Object getDetails() {
        return this.userDetail;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
