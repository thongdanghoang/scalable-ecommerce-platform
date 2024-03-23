package fptu.swp391.shoppingcart.user.profile.exceptions;

public class ConcurrentUpdateException extends RuntimeException{
    public ConcurrentUpdateException() {
        super();
    }

    public ConcurrentUpdateException(String message) {
        super(message);
    }

    public ConcurrentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentUpdateException(Throwable cause) {
        super(cause);
    }
}
