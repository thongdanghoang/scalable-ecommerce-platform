package fptu.swp391.shoppingcart.product.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT")
public class Product extends BaseEntity {

    @Column(name = "SKU", nullable = false, unique = true)
    private String sku;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "DISCOUNT", nullable = false)
    private float discount;

    @Column(name = "NUMBER_OF_SOLD")
    private int numberOfSold;

    @Column(name = "RATED")
    private float rated;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<Quantity> quantities = new ArrayList<>();

    public Product() {
        this.numberOfSold = 0;
        this.rated = 0;
    }

    public Long getGrandTotal() {
        return (long) (this.price * (1 - this.discount));
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}
