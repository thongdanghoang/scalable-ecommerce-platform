package fptu.swp391.shoppingcart.user.authentication.web.impl;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.*;
import fptu.swp391.shoppingcart.user.authentication.service.AuthenticationProviderService;
import fptu.swp391.shoppingcart.user.authentication.service.UserAuthService;
import fptu.swp391.shoppingcart.user.authentication.web.UserAuthController;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpExpiredException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpIncorrectException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpMaxAttemptsExceededException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpStillActiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/user/auth")
public class UserAuthControllerImpl extends AbstractApplicationController implements UserAuthController {
    private final Logger logger = LogManager.getLogger(UserAuthControllerImpl.class);

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AuthenticationProviderService authenticationProviderService;

    @PostMapping("/register")
    @Override
    public ResponseEntity<ApiResponse<?>> register(UserRegisterDTO userRegisterDTO,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        try {
            String username = userAuthService.register(userRegisterDTO);
            request.getSession(true)
                    .setAttribute(
                            SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext()
                    );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("User registered successfully", true, username));
        } catch (DataValidationException e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UsernameAlreadyExists | EmailAlreadyLinked e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/verify-email") // need authenticated user
    @Override
    public ResponseEntity<ApiResponse<?>> verifyMail(String mail, String code) {
        try {
            userAuthService.verifyEmail(mail, code);
            ApiResponse<?> apiResponse = new ApiResponse<>("Email verified successfully", true, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(apiResponse);
        } catch (OtpIncorrectException | DataValidationException | EmailAlreadyLinked e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (OtpExpiredException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpStillActiveException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (OtpSentException e) {
            ApiResponse<?> apiResponse = new ApiResponse<>("Verification code sent successfully", true, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(apiResponse);
        }
    }

    @PostMapping("/forgot-password")
    @Override
    public ResponseEntity<ApiResponse<?>> forgotPasswordByMail(String email, String code, HttpServletResponse response) {
        try {
            addResetPasswordVerificationToken(response, userAuthService.forgotPasswordByMail(email, code));
            ApiResponse<?> apiResponse = new ApiResponse<>("Verification successfully. Please reset your password", true, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(apiResponse);
        } catch (OtpIncorrectException | DataValidationException | EmailNotVerifiedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (OtpExpiredException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpStillActiveException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (OtpSentException e) {
            ApiResponse<?> apiResponse = new ApiResponse<>("Verification code sent successfully", true, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(apiResponse);
        }
    }

    @PostMapping("/reset-password")
    @Override
    public ResponseEntity<ApiResponse<?>> resetPassword(String newPassword,
                                                        String verificationToken,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {
        try {
            userAuthService.resetPassword(newPassword, verificationToken);
            ApiResponse<?> apiResponse = new ApiResponse<>("Password reset successfully", true, null);
            addResetPasswordVerificationToken(response, null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(apiResponse);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void addResetPasswordVerificationToken(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie("verificationResetPassword", value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(600);
        response.addCookie(cookie);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestParam String username, @RequestParam String password,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        Authentication authentication = authenticationProviderService.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession(true)
                .setAttribute(
                        SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext()
                );
        ApiResponse<?> apiResponse = new ApiResponse<>("Login successfully", true, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(){
        SecurityContextHolder.clearContext();
        ApiResponse<?> apiResponse = new ApiResponse<>("Logout successfully", true, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }


}
