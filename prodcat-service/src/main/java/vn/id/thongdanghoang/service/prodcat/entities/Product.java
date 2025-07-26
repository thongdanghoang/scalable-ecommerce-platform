package vn.id.thongdanghoang.service.prodcat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants
public class Product extends AuditableEntity {

  @NotNull
  @Size(min = 1, max = 200)
  @Column(nullable = false)
  private String name;

  @Size(max = 1000)
  private String description;

  @NotNull
  @DecimalMin("0.01")
  @Column(nullable = false)
  private BigDecimal price;

  @Fetch(FetchMode.SUBSELECT)
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "products_categories",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<Category> categories = new LinkedHashSet<>();

}
