package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists() {
        super();
    }

    public UsernameAlreadyExists(String message) {
        super(message);
    }

    public UsernameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExists(Throwable cause) {
        super(cause);
    }
}
