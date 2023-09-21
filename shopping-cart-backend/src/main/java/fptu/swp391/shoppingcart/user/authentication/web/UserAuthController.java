package fptu.swp391.shoppingcart.user.authentication.web;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailAlreadyLinked;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailNotFound;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.authentication.exceptions.otp.*;
import fptu.swp391.shoppingcart.user.authentication.service.UserAuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController extends AbstractApplicationController {
    private final Logger logger = LogManager.getLogger(UserAuthController.class);

    @Autowired
    private UserAuthService userAuthService;

    // forgot password apis:
    // 1. request otp api -> server generate otp and send to email
    // 2. verify otp api -> verify otp and return a token
    // 3. reset password api -> reset password with token
    // service methods: generateOtp, verifyOtp, resetPassword

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestParam String newPassword,
                                                        HttpServletRequest request) {
        try {
            // find reset token from cookie
            Cookie[] cookies = request.getCookies();
            String token = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("reset")) {
                    token = cookie.getValue();
                    break;
                }
            }

            ApiResponse<?> body = new ApiResponse<>(
                    userAuthService.resetPassword(token, newPassword), true, null);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(body);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestParam String email, @RequestParam String code,
                                                    HttpServletResponse response) {
        // Kiểm tra tính hợp lệ của OTP
        try {
            String token = userAuthService.verifyOtp(email, code);

            // Create a new HTTP-only cookie to store the token
            Cookie resetPasswordToken = new Cookie("reset", token);
            resetPasswordToken.setHttpOnly(true); // Make the cookie HTTP-only
//            resetPasswordToken.setSecure(true); // Optional: Set to true if using HTTPS
            resetPasswordToken.setPath("/"); // Set the path as needed
            resetPasswordToken.setMaxAge(60 * 10); // 10 minutes =

            // Add the cookie to the HTTP response
            response.addCookie(resetPasswordToken);

            ApiResponse<String> responseBody = new ApiResponse<>(
                    "OTP verified successfully, please send new password in 10 minutes", true, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseBody);
        }catch(OtpIncorrectException | OtpExpiredException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpVerifiedException e) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestParam String email) {
        try {
            String responseMsg = userAuthService.sendOtp(email);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(responseMsg, true, null));
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpStillActiveException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (OtpVerifiedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EmailNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterDTO userRegisterDTO,
                                                   HttpServletRequest request) {
        try {
            // data validation
            // unique constraint

            userRegisterDTO.setAuthorities(Set.of("ROLE_USER"));
            UserRegisterDTO data = userAuthService.register(userRegisterDTO);
            request.getSession(true)
                    .setAttribute(
                            SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("User registered successfully", true, data));
        } catch (DataValidationException e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UsernameAlreadyExists | EmailAlreadyLinked e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthenticationException e) { // bad credentials
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
