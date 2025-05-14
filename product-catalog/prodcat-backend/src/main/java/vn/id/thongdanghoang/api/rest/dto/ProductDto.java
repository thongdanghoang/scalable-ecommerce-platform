package vn.id.thongdanghoang.api.rest.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import lombok.Builder;

@Builder
public record ProductDto(
    UUID id,
    @PositiveOrZero Long version,
    @NotBlank String sku,
    String name,
    String description,
    @PositiveOrZero BigDecimal price,
    @PositiveOrZero BigDecimal discount,
    boolean active,
    boolean shippable,
    ProductVariantDto variant,
    Set<UUID> categories,
    Set<ProductImageDto> images
) {

  public record ProductVariantDto(
      UUID id,
      @PositiveOrZero Long version,
      String sku,
      BigDecimal additionalPrice,
      String attributes,
      boolean active
  ) {

  }

  public record ProductImageDto
      (
          UUID id,
          @PositiveOrZero Long version,
          String url,
          String alt,
          boolean primary,
          int sortOrder,
          ProductVariantDto variant
      ) {

  }
}
