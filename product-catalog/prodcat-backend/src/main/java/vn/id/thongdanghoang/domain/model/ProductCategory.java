package vn.id.thongdanghoang.domain.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.UUID;
import lombok.*;

@Data
@NoArgsConstructor
@RegisterForReflection
public class ProductCategory {

  private UUID id;
  private long version;
  private String name;
  private String description;
  private ProductCategory parent;

  public ProductCategory(UUID id) {
    this.id = id;
  }
}
