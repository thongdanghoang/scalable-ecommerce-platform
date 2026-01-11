package vn.id.thongdanghoang.user_service.ut;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.entities.UserProfile;
import vn.id.thongdanghoang.user_service.repositories.UserRepository;
import vn.id.thongdanghoang.user_service.securities.FederatedIdentityTokenCustomizer;
import vn.id.thongdanghoang.user_service.services.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

@ExtendWith(MockitoExtension.class)
class FederatedIdentityTokenCustomizerTest {

    @Mock
    private JwtClaimsSet.Builder claimsBuilder;

    @Mock
    private JwtEncodingContext context;

    @InjectMocks
    private FederatedIdentityTokenCustomizer tokenCustomizer;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Test
    void customize_ShouldCreateGithubUser_WhenUserDoesNotExist() {
        // Arrange
        Map<String, Object> attributes = Map.of(
                "id", 12345,
                "login", "githubuser",
                "name", "Github User",
                "email", "test@github.com",
                "location", "Internet");
        OAuth2User principal = new DefaultOAuth2User(Set.of(), attributes, "login");
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(principal, Set.of(), "github");

        when(context.getPrincipal()).thenReturn(token);
        when(context.getClaims()).thenReturn(claimsBuilder);
        when(userRepository.findByProviderId(anyString())).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setId(UUID.randomUUID());

        when(userService.insert(any(User.class))).thenReturn(newUser);
        when(userService.insert(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        tokenCustomizer.customize(context);

        // Assert
        verify(userService, times(1)).insert(any(User.class));
        verify(userService, times(1)).insert(argThat((UserProfile p) -> "test@github.com".equals(p.getEmail()) &&
                "Github".equals(p.getFirstName()) &&
                "User".equals(p.getLastName()) &&
                "Internet".equals(p.getAddress())));
    }

    @Test
    void customize_ShouldCreateGoogleUser_WhenUserDoesNotExist() {
        // Arrange
        Map<String, Object> attributes = Map.of(
                "sub", "google-123",
                "email", "test@gmail.com",
                "given_name", "Test",
                "family_name", "User");
        OidcIdToken idToken = new OidcIdToken("tokenValue", null, null, attributes);
        OAuth2User principal = new DefaultOidcUser(Set.of(), idToken);
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(principal, Set.of(), "google");

        when(context.getPrincipal()).thenReturn(token);
        when(context.getClaims()).thenReturn(claimsBuilder);
        when(userRepository.findByProviderId("google-123")).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setProviderId("google-123");

        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setUser(newUser);

        when(userService.insert(any(User.class))).thenReturn(newUser);
        when(userService.insert(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        tokenCustomizer.customize(context);

        // Assert
        verify(userService, times(1)).insert(any(User.class));
        verify(userService, times(1)).insert(argThat((UserProfile p) -> "test@gmail.com".equals(p.getEmail()) &&
                "Test".equals(p.getFirstName()) &&
                "User".equals(p.getLastName())));
    }

    @Test
    void customize_ShouldThrowException_WhenProviderIsUnsupported() {
        // Arrange
        Map<String, Object> attributes = Map.of("sub", "unknown-123");
        OAuth2User principal = new DefaultOAuth2User(Set.of(), attributes, "sub");
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(principal, Set.of(), "unsupported");

        when(context.getPrincipal()).thenReturn(token);
        when(userRepository.findByProviderId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OAuth2AuthenticationException.class, () -> tokenCustomizer.customize(context));
    }

    @BeforeEach
    void setUp() {
    }
}
