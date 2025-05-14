package vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat;

import vn.id.thongdanghoang.domain.infra.persistence.entity.AuditableEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Entity
@Table
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
    name = ProductEntity.ENTITY_GRAPH_NAME,
    attributeNodes = {
        @NamedAttributeNode(value = "variant"),
        @NamedAttributeNode(value = "images"),
        @NamedAttributeNode(value = "categories"),
    }
)
public class ProductEntity extends AuditableEntity {

  public static final String ENTITY_GRAPH_NAME = "ProductEntity-graph";

  @NotNull
  @Column(unique = true)
  private String sku;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private BigDecimal price;

  @Column
  private BigDecimal discount;

  @Column(name = "is_active")
  private boolean active;

  @Column(name = "is_shippable")
  private boolean shippable;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  private Set<ProductImageEntity> images = new HashSet<>();

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "variant_id")
  private ProductVariantEntity variant;

  @ManyToMany
  @JoinTable(name = "products_categories",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<ProductCategoryEntity> categories = new HashSet<>();
}
