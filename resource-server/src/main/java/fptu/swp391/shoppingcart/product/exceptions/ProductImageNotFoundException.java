package fptu.swp391.shoppingcart.product.exceptions;

public class ProductImageNotFoundException extends RuntimeException {

    public ProductImageNotFoundException() {
        super();
    }

    public ProductImageNotFoundException(String message) {
        super(message);
    }

    public ProductImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductImageNotFoundException(Throwable cause) {
        super(cause);
    }
}
