package vn.id.thongdanghoang.user_service.securities;

import vn.id.thongdanghoang.user_service.entities.OidcProvider;
import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.entities.UserProfile;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;
import vn.id.thongdanghoang.user_service.services.UserService;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
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

    private User createNewUser(OAuth2AuthenticationToken token) {
        var providerId = token.getPrincipal().getName();
        var providerName = token.getAuthorizedClientRegistrationId();
        var attributes = token.getPrincipal().getAttributes();

        OidcProvider oidcProvider;
        try {
            oidcProvider = OidcProvider.fromValue(providerName);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported OAuth provider: {}", providerName);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_provider", "Unsupported OAuth provider: " + providerName, null));
        }

        log.info("Creating new user for provider: {}", providerName);
        var user = new User();
        user.setProviderId(providerId);
        user.setProviderName(oidcProvider);

        var userProfile = new UserProfile();
        userProfile.setUser(userService.insert(user));

        if (OidcProvider.GOOGLE == oidcProvider) {
            userProfile.setEmail((String) attributes.get(OidcProvider.ATTRIBUTE_EMAIL));
            userProfile.setFirstName((String) attributes.get(OidcProvider.ATTRIBUTE_GOOGLE_GIVEN_NAME));
            userProfile.setLastName((String) attributes.get(OidcProvider.ATTRIBUTE_GOOGLE_FAMILY_NAME));
        } else if (OidcProvider.GITHUB == oidcProvider) {
            userProfile.setEmail((String) attributes.get(OidcProvider.ATTRIBUTE_EMAIL));
            userProfile.setAddress((String) attributes.get(OidcProvider.ATTRIBUTE_GITHUB_LOCATION));
            String name = (String) attributes.get(OidcProvider.ATTRIBUTE_GITHUB_NAME);
            if (name != null && !name.isBlank()) {
                String[] parts = name.trim().split(" ", 2);
                userProfile.setFirstName(parts[0]);
                if (parts.length > 1) {
                    userProfile.setLastName(parts[1]);
                }
            } else {
                userProfile.setFirstName((String) attributes.get(OidcProvider.ATTRIBUTE_GITHUB_LOGIN));
            }
        }

        return userService.insert(userProfile).getUser();
    }

    private Map<String, Object> extractClaimsForUser(OAuth2AuthenticationToken token) {
        var providerId = token.getPrincipal().getName();
        var providerName = token.getAuthorizedClientRegistrationId();

        log.debug("Customizing token for provider: {}, principal: {}", providerName, providerId);

        var user = userRepository.findByProviderId(providerId)
                .orElseGet(() -> {
                    try {
                        return createNewUser(token);
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
