package fptu.swp391.shoppingcart.admin.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponseDto {
    private String username;
    private String role;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private LocalDateTime lastAccess;
}
