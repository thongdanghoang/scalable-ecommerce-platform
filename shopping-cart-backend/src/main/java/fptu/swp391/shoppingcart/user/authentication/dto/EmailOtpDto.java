package fptu.swp391.shoppingcart.user.authentication.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class EmailOtpDto {
    @Nullable
    private String code;
    private String email;
}
