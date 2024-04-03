package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class EmailAlreadyLinked extends RuntimeException {

    public EmailAlreadyLinked() {
        super();
    }

    public EmailAlreadyLinked(String message) {
        super(message);
    }

    public EmailAlreadyLinked(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyLinked(Throwable cause) {
        super(cause);
    }
}
