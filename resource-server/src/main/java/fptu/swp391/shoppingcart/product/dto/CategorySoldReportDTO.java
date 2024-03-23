package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

@Data
public class CategorySoldReportDTO {
    private String categoryName;
    private long totalSold;

    public CategorySoldReportDTO(String categoryName, Integer totalSold) {
        this.categoryName = categoryName;
        this.totalSold = totalSold;
    }

    public CategorySoldReportDTO() {
    }
}
