package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class PhoneAlreadyLinked extends RuntimeException {

    public PhoneAlreadyLinked() {
        super();
    }

    public PhoneAlreadyLinked(String message) {
        super(message);
    }

    public PhoneAlreadyLinked(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneAlreadyLinked(Throwable cause) {
        super(cause);
    }
}
