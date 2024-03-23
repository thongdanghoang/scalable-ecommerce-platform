package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductsResponse {
    private List<ProductDto> products;
    private long totalCount;
    public ProductsResponse(List<ProductDto> projects, long totalCount) {
        this.products = projects;
        this.totalCount = totalCount;
    }
}
