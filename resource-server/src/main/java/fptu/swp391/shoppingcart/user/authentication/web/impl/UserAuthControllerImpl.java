package fptu.swp391.shoppingcart.user.authentication.web.impl;

import com.twilio.exception.ApiException;
import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.user.authentication.dto.*;
import fptu.swp391.shoppingcart.user.authentication.exceptions.*;
import fptu.swp391.shoppingcart.user.authentication.service.AuthenticationProviderService;
import fptu.swp391.shoppingcart.user.authentication.service.UserAuthService;
import fptu.swp391.shoppingcart.user.authentication.web.UserAuthController;
import fptu.swp391.shoppingcart.user.otp.exceptions.*;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
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
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("User registered successfully", true, username));
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
    public ResponseEntity<ApiResponse<?>> verifyMail(EmailOtpDto emailOtpDto) {
        try {
            userAuthService.verifyEmail(emailOtpDto.getEmail(), emailOtpDto.getCode());
            ApiResponse<?> apiResponse = new ApiResponse<>("Email verified successfully", true, null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (OtpIncorrectException | DataValidationException | EmailAlreadyLinked e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (OtpExpiredException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpStillActiveException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (OtpNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OtpSentException e) {
            ApiResponse<?> apiResponse = new ApiResponse<>("Verification code sent via mail successfully", true, null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/forgot-password-by-mail")
    @Override
    public ResponseEntity<ApiResponse<?>> forgotPasswordByMail(EmailOtpDto emailOtpDto, HttpServletResponse response) {
        ApiResponse<?> apiResponse;
        try {
            addResetPasswordVerificationToken(
                    response,
                    userAuthService.forgotPasswordByMail(emailOtpDto.getEmail(), emailOtpDto.getCode())
            );
            apiResponse = new ApiResponse<>(
                    "Verification successfully. Please reset your password", true, null
            );
        } catch (OtpIncorrectException | DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EmailNotVerifiedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (OtpExpiredException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (OtpMaxAttemptsExceededException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        } catch (OtpStillActiveException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (OtpSentException e) {
            apiResponse = new ApiResponse<>("Verification code sent via mail successfully", true, null);
        } catch (EmailNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/verify-phone")
    @Override
    public ResponseEntity<ApiResponse<?>> verifyPhone(PhoneOtpDto phoneOtpDto) {
        ApiResponse<?> apiResponse;
        try {
            userAuthService.verifyPhone(phoneOtpDto.getPhone(), phoneOtpDto.getCode());
            apiResponse = new ApiResponse<>("Phone verified successfully", true, null);
        } catch (TwilioServiceException | DataValidationException | OtpIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (PhoneAlreadyLinked e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ApiException e) {
            if (e.getCode().equals(20404)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else if (e.getCode().equals(60202)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
            } else if (e.getCode().equals(60203)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
            }
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OtpSentException e) {
            apiResponse = new ApiResponse<>("Verification code sent successfully", true, null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/forgot-password-by-phone")
    @Override
    public ResponseEntity<ApiResponse<?>> forgotPasswordByPhone(@RequestBody PhoneOtpDto phoneOtpDto, HttpServletResponse response) {
        ApiResponse<?> apiResponse;
        try {
            addResetPasswordVerificationToken(
                    response,
                    userAuthService.forgotPasswordByPhone(phoneOtpDto.getPhone(), phoneOtpDto.getCode())
            );
            apiResponse = new ApiResponse<>(
                    "Verification successfully. Please reset your password", true, null
            );
        } catch (DataValidationException | OtpIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (PhoneNotVerifiedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (TwilioServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        } catch (OtpSentException e) {
            apiResponse = new ApiResponse<>("Verification code sent via phone successfully", true, null);
        } catch (PhoneNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ApiException e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/change-password")
    @Override
    public ResponseEntity<ApiResponse<?>> changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            userAuthService.changePassword(changePasswordDto);
            ApiResponse<?> apiResponse = new ApiResponse<>("Password changed successfully", true, null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (PasswordIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Override
    public ResponseEntity<ApiResponse<?>> resetPassword(ResetPasswordDto resetPasswordDto, String verificationToken, HttpServletRequest request, HttpServletResponse response) {
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password must be the same");
        }
        if (verificationToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token is required");
        }
        try {
            userAuthService.resetPassword(resetPasswordDto.getNewPassword(), verificationToken);
            ApiResponse<?> apiResponse = new ApiResponse<>("Password reset successfully", true, null);
            addResetPasswordVerificationToken(response, null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void addResetPasswordVerificationToken(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie("verificationResetPassword", value);
        cookie.setPath("/");
        cookie.setDomain("thongdanghoang.id.vn");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1800);
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
    public ResponseEntity<ApiResponse<?>> logout() {
        SecurityContextHolder.clearContext();
        ApiResponse<?> apiResponse = new ApiResponse<>("Logout successfully", true, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
