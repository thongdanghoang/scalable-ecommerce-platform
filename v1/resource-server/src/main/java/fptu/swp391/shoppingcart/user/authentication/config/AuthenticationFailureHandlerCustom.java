package fptu.swp391.shoppingcart.user.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fptu.swp391.shoppingcart.ErrorResponse;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.AccountDisabledException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.AccountLockedNotTimeout;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthenticationFailureHandlerCustom implements AuthenticationFailureHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message = exception.getMessage();

        Optional<UserAuthEntity> found = userRepository.findUserByUsername(request.getParameter("username"));
        if (found.isPresent()) {
            UserAuthEntity user = found.get();
            if (!user.isEnabled()) {
                var disabledUntil = Optional.ofNullable(user.getDisabledUntil());
                if (disabledUntil.isPresent() && disabledUntil.get().isBefore(LocalDateTime.now())) {
                    // time out for disabled account
                    user.setNumberOfFailedLoginAttempts(0);
                    user.setDisabledUntil(null);
                    user.setEnabled(true);
                }
            }
            if (user.getNumberOfFailedLoginAttempts() >= 5) {
                if (user.isEnabled()) {
                    user.setDisabledUntil(LocalDateTime.now().plusMinutes(5));
                    user.setEnabled(false);
                    message = "Account is disabled, please try again later in 5 minutes";
                }
            } else {
                user.setNumberOfFailedLoginAttempts(user.getNumberOfFailedLoginAttempts() + 1);
            }
            userRepository.save(user);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage(message);

        if (exception instanceof AccountDisabledException) {
            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        if (exception instanceof AccountLockedNotTimeout) {
            errorResponse.setStatus(429);
        }
        if(exception instanceof BadCredentialsException) {
            errorResponse.setMessage("Username or password is incorrect");
            errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Set the custom date format for LocalDateTime serialization
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        // Serialize the ApiResponse to JSON and write it to the response
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(errorResponse));
            writer.flush();
        }
    }
}
