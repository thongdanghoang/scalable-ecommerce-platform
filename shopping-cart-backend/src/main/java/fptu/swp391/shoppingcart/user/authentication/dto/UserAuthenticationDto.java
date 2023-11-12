package fptu.swp391.shoppingcart.user.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationDto {
    private String username;
    private String role;
}