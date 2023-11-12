package fptu.swp391.shoppingcart.admin.web;

import fptu.swp391.shoppingcart.admin.model.dto.UserReqDto;
import fptu.swp391.shoppingcart.admin.model.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminController {
    ResponseEntity<UserResponseDto> createUser(UserReqDto insert);

    ResponseEntity<UserResponseDto> updateUser(UserReqDto modifier);

    ResponseEntity<List<UserResponseDto>> getAllUsers();

    ResponseEntity<UserResponseDto> getUserByUsername(String username);

    ResponseEntity<UserResponseDto> deleteUserByUsername(String username);
}
