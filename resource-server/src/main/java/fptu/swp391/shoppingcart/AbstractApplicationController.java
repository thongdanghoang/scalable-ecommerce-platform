package fptu.swp391.shoppingcart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public abstract class AbstractApplicationController {
    @ExceptionHandler(ResponseStatusException.class)
    //We implement a @ControllerAdvice globally but also ResponseStatusExceptions locally
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus httpStatus = ex.getStatus();
        String errorMessage = ex.getReason();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setError(httpStatus.getReasonPhrase());
        errorResponse.setMessage(errorMessage);
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
