package fptu.swp391.shoppingcart.user.authentication.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String newPassword;
    private String confirmPassword;
}
