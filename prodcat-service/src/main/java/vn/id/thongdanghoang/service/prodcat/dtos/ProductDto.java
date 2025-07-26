package vn.id.thongdanghoang.service.prodcat.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link vn.id.thongdanghoang.service.prodcat.entities.Product}
 */
public record ProductDto(
    UUID id, int version,
    @Size(min = 1, max = 200) @NotBlank String name,
    @Size(max = 1000) String description,
    @NotNull BigDecimal price,
    Set<UUID> categoryIds
) implements Serializable {

}