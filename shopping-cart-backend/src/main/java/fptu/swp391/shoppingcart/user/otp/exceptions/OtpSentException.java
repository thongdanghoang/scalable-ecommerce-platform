package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpSentException extends Exception {

    public OtpSentException() {
        super();
    }

    public OtpSentException(String message) {
        super(message);
    }

    public OtpSentException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpSentException(Throwable cause) {
        super(cause);
    }
}
