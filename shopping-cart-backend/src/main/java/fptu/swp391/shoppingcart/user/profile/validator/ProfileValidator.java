package fptu.swp391.shoppingcart.user.profile.validator;

import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.profile.dto.ProfileDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProfileValidator {
    public void validateBasicProfileInfo(ProfileDTO profileDTO) throws DataValidationException {
        // Check full name length
        String fullName = profileDTO.getFullName();
        if (fullName != null && (fullName.length() < 2 || fullName.length() > 255)) {
            throw new DataValidationException("Full name must be at least 2 characters and not more than 255 characters.");
        }
        // check special characters, allow vietnamese, chinese, japanese, korean characters ...
        if (fullName != null && !fullName.matches("^[a-zA-Z0-9\\p{L} ]*$")) {
            throw new DataValidationException("Full name must not contain special characters.");
        }

        // Check birthday, it cannot be in the future (after today)
        LocalDate birthday = profileDTO.getBirthday();
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new DataValidationException("Birthday cannot be in the future.");
        }
        // check birthday is not too far in the past
        if (birthday != null && birthday.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new DataValidationException("Birthday cannot be too far in the past.");
        }

        // Check weight and height (must be positive and not too large)
        Integer weight = profileDTO.getWeight();
        Integer height = profileDTO.getHeight();
        if (weight != null && (weight <= 0 || weight > 500)) {
            throw new DataValidationException("Weight must be a positive number and not exceed 500 kg.");
        }
        if (height != null && (height <= 0 || height > 300)) {
            throw new DataValidationException("Height must be a positive number and not exceed 300 cm.");
        }
    }

}
