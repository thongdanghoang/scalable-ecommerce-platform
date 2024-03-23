package fptu.swp391.shoppingcart.user.otp.exceptions;

public class OtpIncorrectException extends Exception {

    public OtpIncorrectException() {
        super();
    }

    public OtpIncorrectException(String message) {
        super(message);
    }

    public OtpIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpIncorrectException(Throwable cause) {
        super(cause);
    }
}
