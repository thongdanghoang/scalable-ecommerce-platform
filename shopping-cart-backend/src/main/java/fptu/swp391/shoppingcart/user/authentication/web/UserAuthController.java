package fptu.swp391.shoppingcart.user.authentication.web;

import fptu.swp391.shoppingcart.user.authentication.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthController {
    ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterDTO userRegisterDTO,
                                            HttpServletRequest request,
                                            HttpServletResponse response);

    ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,
                                                 @CookieValue(value = "verificationResetPassword", required = false) String verificationToken,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response);

    ResponseEntity<ApiResponse<?>> verifyMail(@RequestBody EmailOtpDto emailOtpDto);

    ResponseEntity<ApiResponse<?>> forgotPasswordByMail(@RequestBody EmailOtpDto emailOtpDto,
                                                        HttpServletResponse response);

    ResponseEntity<ApiResponse<?>> verifyPhone(@RequestBody PhoneOtpDto phoneOtpDto);

    ResponseEntity<ApiResponse<?>> forgotPasswordByPhone(@RequestBody PhoneOtpDto phoneOtpDto,
                                                        HttpServletResponse response);

}
