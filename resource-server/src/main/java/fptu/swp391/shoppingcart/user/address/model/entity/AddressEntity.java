package fptu.swp391.shoppingcart.user.address.model.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "USER_ADDRESS")
public class AddressEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "IS_DEFAULT")
    private boolean isDefault;
}
