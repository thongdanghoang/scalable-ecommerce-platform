package vn.id.thongdanghoang.domain.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@RegisterForReflection
public class ProductVariant {

  private UUID id;
  private long version;
  private String sku;
  private BigDecimal additionalPrice;
  private String attributes;
  private boolean active;
}
