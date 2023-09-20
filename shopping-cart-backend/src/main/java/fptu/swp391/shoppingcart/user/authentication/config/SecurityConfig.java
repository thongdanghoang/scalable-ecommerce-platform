package fptu.swp391.shoppingcart.user.authentication.config;

import fptu.swp391.shoppingcart.user.authentication.service.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    @Autowired
    private AuthenticationProviderService authenticationProvider;
    @Autowired
    private AuthenticationFailureHandlerCustom authenticationFailureHandlerCustom;
    @Autowired
    private AuthenticationSuccessHandlerCustom authenticationSuccessHandlerCustom;

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
                    .mvcMatchers("/admin").hasRole("ADMIN")
                    .mvcMatchers("/user").hasRole("USER")
                    .mvcMatchers("/api/user/auth/**").permitAll()
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
        allowedOrigins.add("http://localhost:3000"); // React dev server SWP391

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
