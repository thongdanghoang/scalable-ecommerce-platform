package vn.id.thongdanghoang.n3tk.product.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.id.thongdanghoang.n3tk.common.entities.AbstractAuditableEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product", schema = "n3tk_products")
public class ProductEntity extends AbstractAuditableEntity {

    /**
     * The product title to be displayed on the Shop Page and Product Page.
     */
    @Column(name = "title", nullable = false, length = 75)
    private String title;

    /**
     * The meta title to be used for browser title and SEO.
     */
    @Column(name = "metaTitle", length = 100)
    private String metaTitle;

    /**
     * The slug to form the URL.
     */
    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    /**
     * The summary to mention the key highlights.
     */
    @Column(name = "summary")
    private String summary;

    /**
     * The type to distinguish between the different product types.
     */
    @Column(name = "type", nullable = false)
    private short type;

    /**
     * The Stock Keeping Unit to track the product inventory.
     */
    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @Column(name = "price", nullable = false)
    private double price;

    /**
     * The discount on the product from 0 to 100.
     */
    @Column(name = "discount", nullable = false)
    private double discount;

    /**
     * The available quantity of the product.
     */
    @Column(name = "quantity", nullable = false)
    private short quantity;

    /**
     * It can be used to identify whether the product is publicly available for shopping.
     */
    @Column(name = "shop", nullable = false)
    private boolean publiclyAvailable;

    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    /**
     * It stores the date and time at which the product sale starts.
     */
    @Column(name = "startsAt")
    private LocalDateTime startsAt;

    /**
     * It stores the date and time at which the product sale ends.
     */
    @Column(name = "endsAt")
    private LocalDateTime endsAt;

    /**
     * The column used to store the additional details of the product.
     */
    @Column(name = "content")
    private String content;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Collection<ProductReviewEntity> productReviews = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Collection<ProductMetaEntity> productMetas = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, targetEntity = CategoryEntity.class)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private Collection<CategoryEntity> productCategories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, targetEntity = TagEntity.class)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "tagId"))
    private Collection<TagEntity> productTags = new HashSet<>();
}
