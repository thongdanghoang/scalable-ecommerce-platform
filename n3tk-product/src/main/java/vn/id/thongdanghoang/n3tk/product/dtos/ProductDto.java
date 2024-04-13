package vn.id.thongdanghoang.n3tk.product.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.id.thongdanghoang.n3tk.common.dtos.AbstractBaseDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto extends AbstractBaseDto {
    @NotBlank
    @Size(max = 75)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String slug;

    @NotBlank
    @Size(max = 100)
    private String sku;

    @NotNull
    private short type;

    @NotNull
    private double price;

    @NotNull
    private double discount;

    @NotNull
    private short quantity;

    @NotNull
    private boolean publiclyAvailable;

    @Size(max = 100)
    private String metaTitle;

    private String summary;

    private String content;

    private LocalDateTime publishedAt;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;
}
