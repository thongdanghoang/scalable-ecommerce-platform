package vn.id.thongdanghoang.user_service;

import vn.id.thongdanghoang.user_service.entities.User;
import vn.id.thongdanghoang.user_service.entities.UserAuthenticationProvider;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity(jsr250Enabled = true)
@RegisterReflectionForBinding(UUID[].class)
public class UserServiceRunner {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceRunner.class, args);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 -> oauth2.loginPage("/ui/login.html"));
        http.logout(logout -> logout.logoutSuccessUrl("/ui/login.html?logout"));
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/ui/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        // reference: https://docs.spring.io/spring-authorization-server/reference/guides/how-to-userinfo.html
        var authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()
                .oidc(Customizer.withDefaults());
        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/ui/login.html"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        return http.cors(Customizer.withDefaults()).build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserService userInfoService) {
        return context -> {
            if (context.getPrincipal() instanceof OAuth2AuthenticationToken token) {
                customizeTokenClaims(userInfoService, context, token);
            }
        };
    }

    private void customizeTokenClaims(
            UserService userInfoService,
            JwtEncodingContext context,
            OAuth2AuthenticationToken token) {
        log.debug("Customizing token claims for provider: {}, principal: {}",
                token.getAuthorizedClientRegistrationId(), token.getPrincipal().getName());
        userInfoService.findByProviderLinksId(token.getPrincipal().getName()).ifPresentOrElse(user -> {
            log.debug("Found existing user: {}", user.getId());
            var claims = OidcUserInfo.builder().subject(user.getId().toString()).build().getClaims();
            context.getClaims().claims(claimsConsumer -> claimsConsumer.putAll(claims));
        }, () -> {
            log.info("Creating new user for provider: {}", token.getAuthorizedClientRegistrationId());
            var user = new User();
            var userAuthenticationProvider = new UserAuthenticationProvider();
            userAuthenticationProvider.setProviderId(token.getPrincipal().getName());
            userAuthenticationProvider.setProviderName(token.getAuthorizedClientRegistrationId());
            user.setProviderLinks(new LinkedHashSet<>(Set.of(userAuthenticationProvider)));
            User savedUser = userInfoService.insert(user);
            log.info("Created new user with ID: {}", savedUser.getId());
            var claims = OidcUserInfo.builder().subject(savedUser.getId().toString())
                    .build().getClaims();
            context.getClaims().claims(claimsConsumer -> claimsConsumer.putAll(claims));
        });
    }
}
