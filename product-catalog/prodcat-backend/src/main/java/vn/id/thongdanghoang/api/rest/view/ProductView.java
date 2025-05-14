package vn.id.thongdanghoang.api.rest.view;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductView(
    UUID id,
    Long version,
    String sku,
    String name,
    String description,
    BigDecimal price,
    BigDecimal discount,
    boolean active,
    boolean shippable
) {

}
