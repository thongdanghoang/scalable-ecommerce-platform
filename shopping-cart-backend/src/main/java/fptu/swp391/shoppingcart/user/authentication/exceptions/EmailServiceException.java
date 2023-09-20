package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class EmailServiceException extends RuntimeException {

    public EmailServiceException() {
        super();
    }

    public EmailServiceException(String message) {
        super(message);
    }

    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailServiceException(Throwable cause) {
        super(cause);
    }
}
