package fptu.swp391.shoppingcart.user.authentication.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class PhoneOtpDto {
    @Nullable
    private String code;
    private String phone;
}
