package vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat;

import vn.id.thongdanghoang.domain.infra.persistence.entity.AuditableEntity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Entity
@Table
@Getter
@Setter
@FieldNameConstants
public class ProductCategoryEntity extends AuditableEntity {

  @Column
  private String name;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private ProductCategoryEntity parent;

  @OneToMany(mappedBy = "parent")
  private Set<ProductCategoryEntity> children;
}
