package fptu.swp391.shoppingcart.user.authentication.service;

import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailAlreadyLinked;
import fptu.swp391.shoppingcart.user.authentication.exceptions.OtpSentException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpExpiredException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpIncorrectException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpMaxAttemptsExceededException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpStillActiveException;

public interface UserAuthService {
    String register(UserRegisterDTO userRegisterDTO)
            throws DataValidationException, UsernameAlreadyExists, EmailAlreadyLinked;

    String forgotPasswordByMail(String email, String code)
            throws OtpIncorrectException, OtpExpiredException, OtpMaxAttemptsExceededException, OtpStillActiveException, OtpSentException, DataValidationException;

    void verifyEmail(String email, String code)
            throws OtpStillActiveException, OtpMaxAttemptsExceededException, OtpIncorrectException, OtpExpiredException, DataValidationException, OtpSentException;

    void resetPassword(String newPassword, String token) throws DataValidationException;


}
