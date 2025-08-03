package vn.id.thongdanghoang.service.prodcat.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * View for {@link vn.id.thongdanghoang.service.prodcat.entities.Product}
 */
public record ProductView(
    UUID id, int version,
    LocalDateTime createdDate, LocalDateTime lastModifiedDate,
    String name,
    String description,
    BigDecimal price,
    Set<UUID> categoryIds
) implements Serializable {

}