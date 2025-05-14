package vn.id.thongdanghoang.api.filters;

import vn.id.thongdanghoang.api.exceptions.*;

import jakarta.annotation.Priority;
import jakarta.interceptor.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

@PerformanceMonitoring
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 100)
@Slf4j
public class PerformanceMonitoringInterceptor {

  @AroundInvoke
  public Object aroundInvoke(final InvocationContext context) {
    var annotation = Optional.ofNullable(
            context.getMethod().getAnnotation(PerformanceMonitoring.class))
        .orElse(context.getClass().getAnnotation(PerformanceMonitoring.class));
    try {
      StopWatch watch = null;
      if (annotation != null) {
        watch = new StopWatch();
        watch.start();
      }
      var result = context.proceed();
      logInformation(watch, context.getMethod().getName(), Optional.ofNullable(annotation)
          .map(PerformanceMonitoring::enableExecutionTimeLogging)
          .orElse(false));
      return result;
    } catch (Exception e) {
      if (e instanceof BusinessException businessException) {
        throw businessException;
      }
      throw new TechnicalException(e);
    }
  }

  private void logInformation(StopWatch watch, String methodName, boolean logInformation) {
    if (watch == null) {
      return;
    }
    watch.stop();
    var executionTime = watch.getTime(TimeUnit.MILLISECONDS);
    if (executionTime <= 500) {
      if (logInformation) {
        log.info("Method: {} - Execution time: {}(ms)", methodName, executionTime);
      }
    } else {
      log.warn("Method: {} - Execution time: {}(ms)", methodName, executionTime);
    }
  }
}
