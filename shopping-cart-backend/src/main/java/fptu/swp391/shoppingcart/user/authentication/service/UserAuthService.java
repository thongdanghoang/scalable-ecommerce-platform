package fptu.swp391.shoppingcart.user.authentication.service;

import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.*;
import fptu.swp391.shoppingcart.user.otp.exceptions.*;

public interface UserAuthService {
    String register(UserRegisterDTO userRegisterDTO)
            throws DataValidationException, UsernameAlreadyExists, EmailAlreadyLinked;

    String forgotPasswordByMail(String email, String code)
            throws OtpIncorrectException, OtpExpiredException, OtpMaxAttemptsExceededException, OtpStillActiveException, OtpSentException, DataValidationException, EmailNotFound;

    void verifyEmail(String email, String code)
            throws OtpStillActiveException, OtpMaxAttemptsExceededException, OtpIncorrectException, OtpExpiredException, DataValidationException, OtpSentException, OtpNotFoundException;

    void resetPassword(String newPassword, String token) throws DataValidationException;

    void verifyPhone(String phone, String code) throws TwilioServiceException, DataValidationException, OtpSentException, OtpIncorrectException;

    String forgotPasswordByPhone(String phone, String code) throws DataValidationException, TwilioServiceException, OtpSentException, OtpIncorrectException, PhoneNotFound;
}
