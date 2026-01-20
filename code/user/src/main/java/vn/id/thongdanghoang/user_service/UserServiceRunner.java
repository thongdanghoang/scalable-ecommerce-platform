package vn.id.thongdanghoang.user_service;

import java.util.UUID;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity(jsr250Enabled = true)
@RegisterReflectionForBinding(UUID[].class)
public class UserServiceRunner {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceRunner.class, args);
    }
}
