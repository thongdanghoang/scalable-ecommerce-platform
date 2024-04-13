package vn.id.thongdanghoang.n3tk.common.exceptions.handlers;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.id.thongdanghoang.n3tk.common.exceptions.TechnicalErrorResponse;
import vn.id.thongdanghoang.n3tk.common.utils.MDCContext;

@ControllerAdvice
@Slf4j
public class N3tkOptimisticLockingExceptionHandler {

    private static TechnicalErrorResponse technicalError(OptimisticLockException exception) {
        log.error("Unhandled exception occurred", exception);
        return new TechnicalErrorResponse(
                MDC.get(MDCContext.CORRELATION_ID),
                exception.getMessage()
        );
    }

    @ExceptionHandler({OptimisticLockingFailureException.class})
    public ResponseEntity<TechnicalErrorResponse> handleOptimisticLockingException(OptimisticLockException exception) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(technicalError(exception));
    }
}
