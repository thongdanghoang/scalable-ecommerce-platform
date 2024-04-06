package thongdanghoang.id.vn.n3tkproduct.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thongdanghoang.id.vn.n3tkproduct.common.entities.AbstractBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product_meta", schema = "shopping_cart")
public class ProductMetaEntity extends AbstractBaseEntity {

    @Column(name = "key", nullable = false, length = 50)
    private String key;

    /**
     * store the product metadata.
     */
    @Column(name = "content")
    private String content;

}
