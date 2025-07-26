package vn.id.thongdanghoang.service.prodcat.views;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * View for {@link vn.id.thongdanghoang.service.prodcat.entities.Category}
 */
public record CategoryView(
    UUID id, int version,
    LocalDateTime createdDate, LocalDateTime lastModifiedDate,
    String name,
    String description,
    UUID parentId,
    Set<UUID> childIds
) implements Serializable {

}