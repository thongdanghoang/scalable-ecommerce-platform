package fptu.swp391.shoppingcart.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UserReqDto {
    private String username;
    private String password;
    private Role role;
    private Boolean enabled;

    public enum Role {
        ROLE_ADMIN, ROLE_SHOP_OWNER;
    }
}
