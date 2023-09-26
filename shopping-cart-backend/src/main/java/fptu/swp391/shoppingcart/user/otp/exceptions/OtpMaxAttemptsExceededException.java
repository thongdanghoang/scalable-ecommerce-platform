package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpMaxAttemptsExceededException extends Exception {

    public OtpMaxAttemptsExceededException() {
        super();
    }

    public OtpMaxAttemptsExceededException(String message) {
        super(message);
    }

    public OtpMaxAttemptsExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpMaxAttemptsExceededException(Throwable cause) {
        super(cause);
    }
}
