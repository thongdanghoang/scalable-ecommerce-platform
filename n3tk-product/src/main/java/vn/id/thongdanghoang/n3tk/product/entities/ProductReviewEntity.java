package vn.id.thongdanghoang.n3tk.product.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.id.thongdanghoang.n3tk.common.entities.AbstractAuditableEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product_review", schema = "shopping_cart")
public class ProductReviewEntity extends AbstractAuditableEntity {

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "rating", nullable = false)
    private short rating;

    /**
     * It can be used to identify whether the review is publicly available.
     */
    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @Column(name = "content")
    private String content;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Collection<ProductReviewEntity> productReviewChildren = new HashSet<>();

}
