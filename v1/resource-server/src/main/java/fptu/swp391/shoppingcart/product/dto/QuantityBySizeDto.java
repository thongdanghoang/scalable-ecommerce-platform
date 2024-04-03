package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

@Data
public class QuantityBySizeDto {
    private long quantityId;
    private String size;
    private int quantityInStock;
}
