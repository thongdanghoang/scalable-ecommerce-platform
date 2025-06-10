package vn.id.thongdanghoang.sep.prodcat.filters;

import vn.id.thongdanghoang.sep.prodcat.domain.utils.UuidUtils;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.MDC;

@Provider
@Slf4j
@ActivateRequestContext
public class MonitoringFilter implements ContainerRequestFilter, ContainerResponseFilter {

  public static final String CORRELATION_ID_HEADER_NAME = "correlation-id";
  public static final String PROCESSING_TIME = "processing_time";
  public static final long MAX_RESPONSE_TIME = 500;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    var requestingCorrelationId = requestContext.getHeaders().getFirst(CORRELATION_ID_HEADER_NAME);
    if (StringUtils.isBlank(requestingCorrelationId)) {
      MDC.put(MDCContext.CORRELATION_ID, UuidUtils.randomV7().toString());
    } else {
      MDC.put(MDCContext.CORRELATION_ID, requestingCorrelationId);
    }
    requestContext.setProperty(PROCESSING_TIME,
        LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) {
    try {
      var requestingTime = requestContext.getProperty(PROCESSING_TIME);
      if (requestingTime != null && NumberUtils.isCreatable(requestingTime.toString())) {
        var requestingEpoch = NumberUtils.toLong(requestingTime.toString());
        monitorExecutionTime(requestingEpoch, requestContext.getUriInfo().getPath());
      }
    } finally {
      MDC.remove(MDCContext.CORRELATION_ID);
    }
  }

  public static void monitorExecutionTime(long requestingEpochTime, String request) {
    var now = LocalDateTime.now();
    var executionTime = now.toInstant(ZoneOffset.UTC).toEpochMilli() - requestingEpochTime;
    if (executionTime > MAX_RESPONSE_TIME) {
      log.warn("Request `{}` is processed too long. Execution time: {} ms", request, executionTime);
    } else {
      log.info("Method: `{}` - Execution time: {}(ms)", request, executionTime);
    }
  }
}
