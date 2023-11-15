package fptu.swp391.shoppingcart.order.model.dto;

import lombok.Getter;

@Getter
public enum OrderStatusDto {
    PENDING("Pending"),
    PROCESSING("Processing"),
    CANCELLED("Cancelled"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    RETURNED("Returned");

    private final String lable;

    OrderStatusDto(String value) {
        this.lable = value;
    }
}
