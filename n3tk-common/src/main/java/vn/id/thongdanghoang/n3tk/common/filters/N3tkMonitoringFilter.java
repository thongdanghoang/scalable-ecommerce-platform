package vn.id.thongdanghoang.n3tk.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.id.thongdanghoang.n3tk.common.utils.MDCContext;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class N3tkMonitoringFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String correlationId = request.getHeader(MDCContext.CORRELATION_ID_HEADER_NAME);
        if (correlationId == null || StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put(MDCContext.CORRELATION_ID, correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDCContext.CORRELATION_ID);
        }
    }

}
