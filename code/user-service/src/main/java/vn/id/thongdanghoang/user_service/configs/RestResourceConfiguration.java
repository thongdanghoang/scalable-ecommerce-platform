package vn.id.thongdanghoang.user_service.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestResourceConfiguration implements WebMvcConfigurer {

    public static final String REST_PATH_PREFIX_PATTERN = "/api/**";

    private static final String REST_PATH_PREFIX = "/api";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(REST_PATH_PREFIX, c -> c.isAnnotationPresent(RestController.class));
    }

}
