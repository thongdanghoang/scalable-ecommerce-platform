package fptu.swp391.shoppingcart.user.address.dto;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
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

    @Override
    public String toString() {
        // format: detail, ward, district, province
        return fullName + ", " + phone +
                ", " + addressDetail +
                ", " + ward +
                ", " + district +
                ", " + province;
    }
}
