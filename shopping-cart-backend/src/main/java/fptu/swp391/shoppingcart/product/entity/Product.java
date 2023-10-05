package fptu.swp391.shoppingcart.product.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT")
public class Product extends BaseEntity{

    @Column(name = "SKU", nullable = false, unique = true)
    private String sku;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @OneToMany
    @JoinColumn(name = "PRODUCT_ID")
    private Set<Image> images = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "PRODUCT_ID")
    private Set<Variation> variations = new HashSet<>();
}
