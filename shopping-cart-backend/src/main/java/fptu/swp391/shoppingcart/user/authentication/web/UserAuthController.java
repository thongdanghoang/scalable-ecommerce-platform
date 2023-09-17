package fptu.swp391.shoppingcart.user.authentication.web;

import fptu.swp391.shoppingcart.ErrorResponse;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailAlreadyLinked;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.authentication.service.UserAuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RestControllerAdvice
@RequestMapping("/api/user/auth")
public class UserAuthController {
    private final Logger logger = LogManager.getLogger(UserAuthController.class);

    @Autowired
    private UserAuthService userAuthService;

    //register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterDTO userRegisterDTO,
                                                   HttpServletRequest request) {
        try {
            // data validation
            // unique constraint

            userRegisterDTO.setAuthorities(Set.of("ROLE_USER"));
            userAuthService.register(userRegisterDTO);
            request.getSession(true)
                    .setAttribute(
                            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("User registered successfully", true, null));
        } catch (DataValidationException e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UsernameAlreadyExists | EmailAlreadyLinked e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthenticationException e) { // bad credentials
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) { // server error
            // Log the exception for debugging and return a 500 Internal Server Error
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during registration. Please try again later.");
        }
    }

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

        //Log the error
        logger.error("ResponseStatusException: HTTP {} {}",
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                ex);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
