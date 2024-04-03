package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException() {
        super();
    }

    public EmailNotVerifiedException(String message) {
        super(message);
    }

    public EmailNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotVerifiedException(Throwable cause) {
        super(cause);
    }
}
