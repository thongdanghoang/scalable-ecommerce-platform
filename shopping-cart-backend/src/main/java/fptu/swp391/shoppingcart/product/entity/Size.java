package fptu.swp391.shoppingcart.product.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "SIZE")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SIZE_NAME", nullable = false)
    private String sizeName;

    public Size() {
    }

    public Size(String sizeName) {
        this.sizeName = sizeName;
    }
}
