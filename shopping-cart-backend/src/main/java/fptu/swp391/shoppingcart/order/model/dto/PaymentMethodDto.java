package fptu.swp391.shoppingcart.order.model.dto;

import lombok.Getter;

@Getter
public enum PaymentMethodDto {
    CASH_ON_DELIVERY("Cash on delivery"),
    MOMO("Momo"),
    CREDIT_CARD("Credit card");

    private final String lable;

    PaymentMethodDto(String value) {
        this.lable = value;
    }
}
