package fptu.swp391.shoppingcart.user.authentication.exceptions.otp;

public class OtpExpiredException extends Exception {

    public OtpExpiredException() {
        super();
    }

    public OtpExpiredException(String message) {
        super(message);
    }

    public OtpExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpExpiredException(Throwable cause) {
        super(cause);
    }
}
