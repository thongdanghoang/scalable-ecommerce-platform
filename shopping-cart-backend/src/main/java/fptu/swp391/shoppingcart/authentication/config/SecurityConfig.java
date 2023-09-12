package fptu.swp391.shoppingcart.authentication.config;

import fptu.swp391.shoppingcart.authentication.service.AuthenticationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProviderService authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .csrf().disable()
                .authorizeRequests()
                    .mvcMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                .and().httpBasic();
//                .and().formLogin();
        //@formatter:on
    }
}
