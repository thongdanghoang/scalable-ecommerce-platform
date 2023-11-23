package fptu.swp391.shoppingcart.order.model.entity;

import fptu.swp391.shoppingcart.user.address.model.entity.AddressType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ORDER_ADDRESS")
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail;//   Tên đường, toà nhà, số nhà
    @Column(name = "PROVINCE")
    private String province;//    Tỉnh/Thành phố    ->  Province/City
    @Column(name = "DISTRICT")
    private String district;//    Quận/Huyện	    ->  District/County
    @Column(name = "WARD")
    private String ward;    //    Phường/Xã	        ->  Ward/Commune

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private AddressType type;
}
