package fptu.swp391.shoppingcart.order.model.dto;

import lombok.Getter;

@Getter
public enum DeliveryMethodDto {
    STANDARD_DELIVERY("Standard Delivery");

    private final String label;

    DeliveryMethodDto(String value) {
        this.label = value;
    }

}
