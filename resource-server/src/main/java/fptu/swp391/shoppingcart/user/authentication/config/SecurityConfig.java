package fptu.swp391.shoppingcart.user.authentication.config;

import fptu.swp391.shoppingcart.user.authentication.service.AuthenticationProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationProviderService authenticationProvider;
    private final AuthenticationFailureHandlerCustom authenticationFailureHandlerCustom;
    private final AuthenticationSuccessHandlerCustom authenticationSuccessHandlerCustom;

    public SecurityConfig(AuthenticationProviderService authenticationProvider, AuthenticationFailureHandlerCustom authenticationFailureHandlerCustom, AuthenticationSuccessHandlerCustom authenticationSuccessHandlerCustom) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationFailureHandlerCustom = authenticationFailureHandlerCustom;
        this.authenticationSuccessHandlerCustom = authenticationSuccessHandlerCustom;
    }

    private static void commence(HttpServletRequest request,
                                 HttpServletResponse response,
                                 AuthenticationException authException)
            throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .cors().and()
                .csrf().disable() // TODO : Enable CSRF protection for best security
                .authorizeRequests()
                    .mvcMatchers("/admin/**").hasRole("ADMIN")
                    .mvcMatchers("/user/**").hasRole("USER")
                    .mvcMatchers("/cart/**").hasRole("USER")
                    .antMatchers("/api/orders/all").hasRole("SHOP_OWNER")
                    .antMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasRole("SHOP_OWNER")
                    .antMatchers(HttpMethod.GET, "/api/orders/status/**").hasAnyRole( "USER", "SHOP_OWNER")
                    .antMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("USER", "SHOP_OWNER")
                    .mvcMatchers(HttpMethod.POST, "/api/products/**").hasRole("SHOP_OWNER")
                    .mvcMatchers(HttpMethod.PUT, "/api/products").hasRole("SHOP_OWNER")
                    .mvcMatchers(HttpMethod.GET, "/api/products/report").hasRole("SHOP_OWNER")
                    .mvcMatchers(HttpMethod.GET, "/api/products/statistic").hasRole("SHOP_OWNER")
                    .antMatchers(HttpMethod.POST, "/api/orders/checkout").hasRole("USER")
                    .antMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/api/orders/payment/{orderId}").hasRole("USER")
                    .mvcMatchers("/api/user/auth/verify-email").authenticated()
                    .mvcMatchers("/api/user/auth/verify-phone").authenticated()
                    .mvcMatchers("/api/user/auth/change-password").authenticated()
                    .mvcMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                    .mvcMatchers("/api/user/auth/**").permitAll()
                    .mvcMatchers("/api/products/**").permitAll()
                    .mvcMatchers("/login/oauth2/code/google/**").permitAll()
                    .anyRequest().authenticated().and()
                .formLogin()
                    .failureHandler(authenticationFailureHandlerCustom)
                    .successHandler(authenticationSuccessHandlerCustom)
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(SecurityConfig::commence);
        //@formatter:on
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Configure allowed origins
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("https://thongdanghoang.id.vn/isc-301"); // React dev server SWP391
        allowedOrigins.add("*");
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // Allow sending cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // CORS configuration for other paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
