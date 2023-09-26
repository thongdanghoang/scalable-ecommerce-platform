package fptu.swp391.shoppingcart.user.authentication.web;

import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
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

    ResponseEntity<ApiResponse<?>> resetPassword(@RequestParam String newPassword,
                                                 @CookieValue("verificationResetPassword") String verificationToken,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response);

    ResponseEntity<ApiResponse<?>> verifyMail(@RequestParam String mail,
                                              @RequestParam(required = false) String code);

    ResponseEntity<ApiResponse<?>> forgotPasswordByMail(@RequestParam String email,
                                                        @RequestParam(required = false) String code,
                                                        HttpServletResponse response);
}
