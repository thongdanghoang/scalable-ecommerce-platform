package vn.id.thongdanghoang.api.filters;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Will log[warn] if the execution time is greater than 500(ms)
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PerformanceMonitoring {
    /**
     * Always log execution time
     */
    @Nonbinding boolean enableExecutionTimeLogging() default false;
}
