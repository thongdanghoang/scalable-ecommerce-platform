package vn.id.thongdanghoang.sep.prodcat.domain.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.UUID;
import lombok.Data;

@Data
@RegisterForReflection
public class ProductImage {

  private UUID id;
  private long version;
  private String url;
  private String alt;
  private boolean primary;
  private int sortOrder;
  private ProductVariant variant;
}
