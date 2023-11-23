package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDto {

    private long id;
    private String sku;

    private String name;

    private int price;
    private float discount;

    private int numberOfSold;
    private float rated;

    private String category;

    private List<ClassifyClotheDto> classifyClothes;

    private String description;
}
