package fptu.swp391.shoppingcart.user.address.web.impl;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import fptu.swp391.shoppingcart.user.address.model.service.AddressService;
import fptu.swp391.shoppingcart.user.address.web.AddressController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import fptu.swp391.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user/address")
public class AddressControllerImpl extends AbstractApplicationController implements AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    @Override
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(Principal principal, AddressDto addressDto) {
        try {
            addressService.addAddress(addressDto, principal.getName());
            ApiResponse<AddressDto> response = new ApiResponse<>();
            response.setMessage("Add address successfully");
            response.setSuccess(true);
            response.setData(addressDto);
            return ResponseEntity.ok(response);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddress(Principal principal) {
        ApiResponse<List<AddressDto>> response = new ApiResponse<>();
        response.setMessage("Get all address successfully");
        response.setSuccess(true);
        response.setData(addressService.getAllAddress(principal.getName()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @Override
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(Principal principal, AddressDto addressDto) {
        try {
            AddressDto data = addressService.updateAddress(addressDto, principal.getName());
            ApiResponse<AddressDto> response = new ApiResponse<>();
            response.setMessage("Update address successfully");
            response.setSuccess(true);
            response.setData(data);
            return ResponseEntity.ok(response);
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ConcurrentUpdateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<ApiResponse<?>> deleteAddress(Principal principal, AddressDto addressDto) {
        try {
            boolean isDeleted = addressService.deleteAddress(addressDto, principal.getName());
            return ResponseEntity.ok(new ApiResponse<>("Delete address successfully", isDeleted, null));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
