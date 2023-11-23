package fptu.swp391.shoppingcart.order.model.exception;

public class PaymentPendingException extends RuntimeException {

    public PaymentPendingException() {
        super();
    }

    public PaymentPendingException(String message) {
        super(message);
    }

    public PaymentPendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentPendingException(Throwable cause) {
        super(cause);
    }
}
