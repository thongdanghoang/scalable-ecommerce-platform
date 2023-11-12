package fptu.swp391.shoppingcart.admin.service;

import fptu.swp391.shoppingcart.admin.model.dto.UserReqDto;
import fptu.swp391.shoppingcart.admin.model.dto.UserResponseDto;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;

import java.util.List;

public interface AdminService {
    UserResponseDto insert(UserReqDto user) throws DataValidationException;

    UserResponseDto modify(UserReqDto user) throws DataValidationException;

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByUsername(String username);

    UserResponseDto destroy(String username);
}
