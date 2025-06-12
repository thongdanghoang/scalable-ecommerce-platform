package vn.id.thongdanghoang.sep.prodcat.entity.prodcat;

import vn.id.thongdanghoang.sep.prodcat.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
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
