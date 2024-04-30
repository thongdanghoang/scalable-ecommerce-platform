package vn.id.thongdanghoang.n3tk.product.entities;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.id.thongdanghoang.n3tk.common.entities.AbstractBaseEntity;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "category", schema = "n3tk_products")
public class CategoryEntity extends AbstractBaseEntity {

    @Basic
    @Column(name = "title", nullable = false, length = 75)
    private String title;

    /**
     * The meta title to be used for browser title and SEO.
     */
    @Basic
    @Column(name = "meta_title", length = 100)
    private String metaTitle;

    /**
     * The category slug to form the URL.
     */
    @Basic
    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Basic
    @Column(name = "content")
    private String content;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Collection<CategoryEntity> categoryChildren = new HashSet<>();

    @ManyToMany(mappedBy = "productCategories")
    private Collection<ProductEntity> products = new HashSet<>();
}
