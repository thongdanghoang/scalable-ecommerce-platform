package fptu.swp391.shoppingcart.user.address.web;

import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

public interface AddressController {
    ResponseEntity<ApiResponse<AddressDto>> addAddress(Principal principal, @RequestBody AddressDto addressDto);

    ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddress(Principal principal);

    ResponseEntity<ApiResponse<AddressDto>> updateAddress(Principal principal, @RequestBody AddressDto addressDto);

    ResponseEntity<ApiResponse<?>> deleteAddress(Principal principal, @RequestBody AddressDto addressDto);

}
