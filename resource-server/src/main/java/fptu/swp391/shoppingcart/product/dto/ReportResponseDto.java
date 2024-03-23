package fptu.swp391.shoppingcart.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportResponseDto {
    private String name;
    private List<CategorySoldReportDTO> categorySoldReportDTOS;
}
