package fptu.swp391.shoppingcart.user.address.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddressDto {
    private long id;
    private int version;
    private String fullName;
    private String phone;
    private String addressDetail;
    private String province;
    private String district;
    private String ward;
    private AddressTypeDto type;
    private boolean isDefault;

}
