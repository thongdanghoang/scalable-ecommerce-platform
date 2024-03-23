package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpVerifiedException extends Exception {

    public OtpVerifiedException() {
        super();
    }

    public OtpVerifiedException(String message) {
        super(message);
    }

    public OtpVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpVerifiedException(Throwable cause) {
        super(cause);
    }
}
