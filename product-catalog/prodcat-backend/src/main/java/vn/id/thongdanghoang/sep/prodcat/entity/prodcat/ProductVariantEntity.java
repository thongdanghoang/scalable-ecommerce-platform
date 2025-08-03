package vn.id.thongdanghoang.sep.prodcat.entity.prodcat;

import vn.id.thongdanghoang.sep.prodcat.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

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
