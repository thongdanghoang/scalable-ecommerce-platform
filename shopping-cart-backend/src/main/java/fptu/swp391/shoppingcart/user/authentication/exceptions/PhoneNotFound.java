package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class PhoneNotFound extends Exception {

    public PhoneNotFound() {
        super();
    }

    public PhoneNotFound(String message) {
        super(message);
    }

    public PhoneNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNotFound(Throwable cause) {
        super(cause);
    }
}
