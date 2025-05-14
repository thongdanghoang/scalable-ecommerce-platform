package vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat;

import vn.id.thongdanghoang.domain.infra.persistence.entity.AuditableEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table
@Getter
@Setter
public class ProductVariantEntity extends AuditableEntity {

  @NotNull
  @Column(unique = true)
  private String sku;

  @Column
  private BigDecimal additionalPrice;

  @Column
  private String attributes;

  @Column(name = "is_active")
  private boolean active;

}
