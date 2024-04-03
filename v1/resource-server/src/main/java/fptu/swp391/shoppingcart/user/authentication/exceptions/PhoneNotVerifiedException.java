package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class PhoneNotVerifiedException extends RuntimeException {

    public PhoneNotVerifiedException() {
        super();
    }

    public PhoneNotVerifiedException(String message) {
        super(message);
    }

    public PhoneNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNotVerifiedException(Throwable cause) {
        super(cause);
    }
}
