package fptu.swp391.shoppingcart.order.model.entity.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    CANCELLED("Cancelled"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    RETURNED("Returned");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

}
