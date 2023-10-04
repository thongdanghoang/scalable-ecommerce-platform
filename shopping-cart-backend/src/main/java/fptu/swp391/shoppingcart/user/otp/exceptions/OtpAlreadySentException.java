package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpAlreadySentException extends Exception {

    public OtpAlreadySentException() {
        super();
    }

    public OtpAlreadySentException(String message) {
        super(message);
    }

    public OtpAlreadySentException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpAlreadySentException(Throwable cause) {
        super(cause);
    }
}
