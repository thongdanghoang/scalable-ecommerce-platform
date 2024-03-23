package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpNotFoundException extends Exception {

    public OtpNotFoundException() {
        super();
    }

    public OtpNotFoundException(String message) {
        super(message);
    }

    public OtpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpNotFoundException(Throwable cause) {
        super(cause);
    }
}
