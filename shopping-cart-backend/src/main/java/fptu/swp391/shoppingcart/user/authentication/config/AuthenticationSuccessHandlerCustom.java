package fptu.swp391.shoppingcart.user.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationSuccessHandlerCustom implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK); // Đặt HTTP status code là 200 OK
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        userRepository.findUserByUsername(request.getParameter("username"))
                .ifPresent(user -> {
                    user.setNumberOfFailedLoginAttempts(0);
                    user.setDisabledUntil(null);
                    user.setEnabled(true);
                    userRepository.save(user);
                });

        // Serialize the ApiResponse to JSON and write it to the response
        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(
                    new ApiResponse<>("Authentication successful", true, authentication.getName())
            ));
            writer.flush();
        }
    }
}
