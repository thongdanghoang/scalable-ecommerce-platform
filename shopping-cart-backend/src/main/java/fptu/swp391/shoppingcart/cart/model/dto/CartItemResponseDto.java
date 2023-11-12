package fptu.swp391.shoppingcart.cart.model.dto;

import fptu.swp391.shoppingcart.product.dto.ProductDto;
import lombok.Data;

@Data
public class CartItemResponseDto {
    private ProductDto product;
    private ClassificationDto classification;
    private int amount;
}
