package vn.id.thongdanghoang.sep.prodcat.entity.prodcat;

import vn.id.thongdanghoang.sep.prodcat.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
