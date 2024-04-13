package vn.id.thongdanghoang.n3tk.common.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.id.thongdanghoang.n3tk.common.exceptions.N3tkBusinessException;
import vn.id.thongdanghoang.n3tk.common.exceptions.N3tkRuntimeException;
import vn.id.thongdanghoang.n3tk.common.exceptions.ServerErrorResponse;
import vn.id.thongdanghoang.n3tk.common.exceptions.TechnicalErrorResponse;
import vn.id.thongdanghoang.n3tk.common.utils.MDCContext;

@ControllerAdvice
@Slf4j
public class N3tkTechnicalExceptionHandler {

    static TechnicalErrorResponse technicalError(Throwable exception, String errorMsg) {
        log.error("Unhandled exception occurred", exception);
        return new TechnicalErrorResponse(
                MDC.get(MDCContext.CORRELATION_ID),
                errorMsg
        );
    }

    @ExceptionHandler({N3tkRuntimeException.class})
    public ResponseEntity<ServerErrorResponse> handleTechnicalExceptionHandler(N3tkRuntimeException n3tkRuntimeException) {
        if (n3tkRuntimeException.getCause() instanceof N3tkBusinessException n3tkBusinessException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(N3tkBusinessExceptionHandler.businessError(n3tkBusinessException));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(technicalError(n3tkRuntimeException, n3tkRuntimeException.getMessage()));
    }
}
