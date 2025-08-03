package vn.id.thongdanghoang.service.prodcat.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link vn.id.thongdanghoang.service.prodcat.entities.Category}
 */
public record CategoryDto(
    UUID id, int version,
    @NotNull @Size(min = 1, max = 100) String name,
    @Size(max = 500) String description,
    UUID parentId,
    Set<UUID> childIds
) implements Serializable {

}