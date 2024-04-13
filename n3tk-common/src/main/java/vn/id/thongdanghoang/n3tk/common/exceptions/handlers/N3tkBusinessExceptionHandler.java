package vn.id.thongdanghoang.n3tk.common.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.id.thongdanghoang.n3tk.common.exceptions.BusinessErrorResponse;
import vn.id.thongdanghoang.n3tk.common.exceptions.N3tkBusinessException;
import vn.id.thongdanghoang.n3tk.common.utils.MDCContext;

@ControllerAdvice
@Slf4j
public class N3tkBusinessExceptionHandler {

    static BusinessErrorResponse businessError(N3tkBusinessException exception) {
        return new BusinessErrorResponse(
                MDC.get(MDCContext.CORRELATION_ID),
                exception.getField(),
                exception.getArgs()
        );
    }

    @ExceptionHandler({N3tkBusinessException.class})
    public ResponseEntity<BusinessErrorResponse> handleBusinessException(N3tkBusinessException exception) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(businessError(exception));
    }
}
