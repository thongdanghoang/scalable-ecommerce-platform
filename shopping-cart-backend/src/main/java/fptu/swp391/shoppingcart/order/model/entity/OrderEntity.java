package fptu.swp391.shoppingcart.order.model.entity;

import fptu.swp391.shoppingcart.order.model.entity.enums.DeliveryMethod;
import fptu.swp391.shoppingcart.order.model.entity.enums.OrderStatus;
import fptu.swp391.shoppingcart.order.model.entity.enums.PaymentMethod;
import fptu.swp391.shoppingcart.user.address.model.entity.AddressEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "PURCHASE_ORDER")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserAuthEntity user;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_ENTITY_ID")
    private AddressEntity addressEntity;

    @OneToMany
    @JoinColumn(name = "ORDER_ENTITY_ID")
    private Set<OrderItemEntity> orderItems = new LinkedHashSet<>();

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updatedDate;

    @OneToOne(mappedBy = "orderEntity", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    private PaymentDetail paymentDetail;

    @Column(name = "DELIVERY_METHOD")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "TOTAL", nullable = false)
    private long total;

    @Column(name = "GRAND_TOTAL", nullable = false)
    private long grandTotal;

    @Column(name = "SHIPPING_FEE", nullable = false)
    private long shippingFee;

    @Column(name = "DISCOUNT", nullable = false)
    private long discount;

    public OrderEntity() {
        this.status = OrderStatus.PENDING;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("createdDate", createdDate)
                .append("updatedDate", updatedDate)
                .append("deliveryMethod", deliveryMethod)
                .append("status", status)
                .append("total", total)
                .append("grandTotal", grandTotal)
                .append("shippingFee", shippingFee)
                .append("discount", discount)
                .toString();
    }

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updatedDate = createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
