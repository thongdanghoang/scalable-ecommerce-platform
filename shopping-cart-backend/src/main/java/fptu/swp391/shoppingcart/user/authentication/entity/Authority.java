package fptu.swp391.shoppingcart.user.authentication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "AUTHORITY")
public class Authority extends BaseEntity {
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
}
