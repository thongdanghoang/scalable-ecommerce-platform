package fptu.swp391.shoppingcart.order.model.entity.enums;

public enum PaymentMethod {
    CASH_ON_DELIVERY("Cash on delivery"),
    MOMO("Momo"),
    CREDIT_CARD("Credit card");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
