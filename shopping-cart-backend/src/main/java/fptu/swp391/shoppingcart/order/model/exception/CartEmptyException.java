package fptu.swp391.shoppingcart.order.model.exception;

public class CartEmptyException extends RuntimeException {

    public CartEmptyException() {
        super();
    }

    public CartEmptyException(String message) {
        super(message);
    }

    public CartEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartEmptyException(Throwable cause) {
        super(cause);
    }
}
