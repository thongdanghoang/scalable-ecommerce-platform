package vn.id.thongdanghoang.n3tk.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.id.thongdanghoang.n3tk.common.entities.AbstractAuditableEntity;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tag", schema = "shopping_cart")
public class TagEntity extends AbstractAuditableEntity {

    @Column(name = "title", nullable = false, length = 75)
    private String title;

    /**
     * The meta title to be used for browser title and SEO.
     */
    @Column(name = "metaTitle", length = 100)
    private String metaTitle;

    /**
     * The category slug to form the URL.
     */
    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Column(name = "content", length = -1)
    private String content;

    @ManyToMany(mappedBy = "productTags")
    private Collection<ProductEntity> products = new HashSet<>();
}
