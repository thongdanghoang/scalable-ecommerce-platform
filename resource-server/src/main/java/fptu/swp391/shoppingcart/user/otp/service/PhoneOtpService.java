package fptu.swp391.shoppingcart.user.otp.service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import fptu.swp391.shoppingcart.user.authentication.exceptions.TwilioServiceException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpIncorrectException;
import org.springframework.beans.factory.annotation.Value;

@org.springframework.stereotype.Service
public class PhoneOtpService {

    @Value("${twilio.service.sid}")
    private String SERVICE_SID;
    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;
    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    public void sendVerification(String target) throws TwilioServiceException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(
                        SERVICE_SID,
                        target,
                        "sms")
                .create();
        if (!verification.getStatus().equals("pending")) {
            throw new TwilioServiceException("Twilio service error");
        }
    }

    public void checkVerification(String target, String otp) throws OtpIncorrectException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        VerificationCheck verificationCheck = VerificationCheck.creator(SERVICE_SID)
                .setTo(target)
                .setCode(otp)
                .create();
        if (!verificationCheck.getStatus().equals("approved")) {
            throw new OtpIncorrectException("OTP incorrect");
        }
    }
}
