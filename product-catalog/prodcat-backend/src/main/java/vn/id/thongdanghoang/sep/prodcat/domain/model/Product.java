package vn.id.thongdanghoang.sep.prodcat.domain.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
@RegisterForReflection
public class Product {

  private UUID id;
  private long version;
  private String sku;
  private String name;
  private String description;
  private BigDecimal price;
  private BigDecimal discount;
  private boolean active;
  private boolean shippable;
  private ProductVariant variant;
  private Set<ProductCategory> categories = new HashSet<>();
  private Set<ProductImage> images = new HashSet<>();
}
