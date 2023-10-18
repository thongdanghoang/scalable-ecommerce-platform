package fptu.swp391.shoppingcart.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_CATEGORY_ID")
    private Category parentCategory;

    public String getFullName() {
        if (parentCategory == null) {
            return name;
        }
        return parentCategory.getFullName() + ">" + name;
    }
}
