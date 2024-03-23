package fptu.swp391.shoppingcart.user.authentication.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "AUTHORITY")
public class Authority extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    public Authority(String name) {
        this.name = name;
    }

    public Authority() {
    }
}
