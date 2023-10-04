package fptu.swp391.shoppingcart.user.authentication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;

    @JsonIgnore
    private Set<String> authorities;
}
