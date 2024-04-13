package vn.id.thongdanghoang.n3tk.common.exceptions.handlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class N3tkExceptionHandler {

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleBusinessException(Throwable exception) {
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(N3tkTechnicalExceptionHandler.technicalError(constraintViolationException, "Invalid payload"));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(N3tkTechnicalExceptionHandler.technicalError(exception, exception.getMessage()));
    }
}
