package vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat;

import vn.id.thongdanghoang.domain.infra.persistence.entity.AuditableEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
public class ProductImageEntity extends AuditableEntity {

  @Column
  private String url;

  @Column
  private String alt;

  @Column(name = "is_primary")
  private boolean primary;

  @Column
  private int sortOrder;

  @ManyToOne
  @JoinColumn(name = "variant_id")
  private ProductVariantEntity variant;
}
