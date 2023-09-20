package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class EmailNotFound extends RuntimeException {

    public EmailNotFound() {
        super();
    }

    public EmailNotFound(String message) {
        super(message);
    }

    public EmailNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotFound(Throwable cause) {
        super(cause);
    }
}
