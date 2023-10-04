package fptu.swp391.shoppingcart.user.profile.web;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.profile.dto.ProfileDTO;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import fptu.swp391.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import fptu.swp391.shoppingcart.user.profile.service.ProfileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user/profile")
public class ProfileController extends AbstractApplicationController {
    private final Logger logger = LogManager.getLogger(ProfileController.class);

    @Autowired
    private ProfileService service;


    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile() {
        ApiResponse<ProfileDTO> response = new ApiResponse<>();
        ProfileDTO profile = service.getProfile();
        response.setData(profile);
        response.setSuccess(true);
        response.setMessage("Profile retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/basic")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(@RequestBody ProfileDTO profile) {
        ApiResponse<ProfileDTO> response = new ApiResponse<>();
        try {
            ProfileDTO updatedProfile = service.updateBasic(profile);
            response.setData(updatedProfile);
            response.setSuccess(true);
            response.setMessage("Profile updated successfully");
        } catch (DataValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AuthorizationException ae) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ae.getMessage());
        } catch (ConcurrentUpdateException cue) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, cue.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
