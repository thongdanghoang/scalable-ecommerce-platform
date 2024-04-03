package fptu.swp391.shoppingcart.cart.model.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "CART")
public class Cart extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Set<CartItem> items = new LinkedHashSet<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "user_auth_entity_id", nullable = false)
    private UserAuthEntity userAuthEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        return new EqualsBuilder().append(items, cart.items).append(userAuthEntity, cart.userAuthEntity).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(items).append(userAuthEntity).toHashCode();
    }
}
