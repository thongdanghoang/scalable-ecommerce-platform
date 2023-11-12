package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductAddingDto {
    private long id;
    private String name;
    private String sku;
    private int price;
    private float discount;
    private long categoryId;
    private String description;
    private List<ClassifyClotheDto> classifyClothes;
}