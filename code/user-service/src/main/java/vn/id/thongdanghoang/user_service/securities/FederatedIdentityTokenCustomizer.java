package vn.id.thongdanghoang.user_service.securities;

import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.entities.UserProfile;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;
import vn.id.thongdanghoang.user_service.services.UserService;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FederatedIdentityTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getPrincipal() instanceof OAuth2AuthenticationToken token) {
            var claims = extractClaimsForUser(token);
            context.getClaims().claims(existingClaims -> existingClaims.putAll(claims));
        }
    }

    private User createNewUser(String providerId, String providerName) {
        log.info("Creating new user for provider: {}", providerName);
        var user = new User();
        user.setProviderId(providerId);
        user.setProviderName(providerName);
        var userProfile = new UserProfile();
        userProfile.setUser(userService.insert(user));
        return userService.insert(userProfile).getUser();
    }

    private Map<String, Object> extractClaimsForUser(OAuth2AuthenticationToken token) {
        var providerId = token.getPrincipal().getName();
        var providerName = token.getAuthorizedClientRegistrationId();

        log.debug("Customizing token for provider: {}, principal: {}", providerName, providerId);

        var user = userRepository.findByProviderId(providerId)
                .orElseGet(() -> {
                    try {
                        return createNewUser(providerId, providerName);
                    } catch (DataIntegrityViolationException e) {
                        log.debug("Concurrent creation detected for providerId: {}. Retrying find.", providerId);
                        return userRepository.findByProviderId(providerId)
                                .orElseThrow(
                                        () -> new RuntimeException("User creation failed and user not found after retry", e));
                    }
                });

        return OidcUserInfo.builder()
                .subject(user.getId().toString())
                .build()
                .getClaims();
    }
}
