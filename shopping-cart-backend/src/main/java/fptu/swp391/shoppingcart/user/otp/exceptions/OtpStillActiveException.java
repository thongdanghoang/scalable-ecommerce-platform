package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpStillActiveException extends Exception {

    public OtpStillActiveException() {
        super();
    }

    public OtpStillActiveException(String message) {
        super(message);
    }

    public OtpStillActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpStillActiveException(Throwable cause) {
        super(cause);
    }
}
