package fptu.swp391.shoppingcart.user.authentication.validator;

import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationValidator {
    public void validate(UserRegisterDTO userRegisterDTO) throws DataValidationException {
        // empty check
        if (userRegisterDTO.getUsername().isEmpty() || userRegisterDTO.getPassword().isEmpty() || userRegisterDTO.getEmail().isEmpty())
            throw new DataValidationException("Username, password and email must not be empty");

        checkPassword(userRegisterDTO.getPassword());

        // USERNAME from 6 to 20 characters
        if (userRegisterDTO.getUsername().length() < 6)
            throw new DataValidationException("Username must be at least 6 characters");
        if (userRegisterDTO.getUsername().length() > 20)
            throw new DataValidationException("Username must be at most 20 characters");
        // Usernames must only contain certain characters, such as letters, numbers, and underscores.
        String usernameRegex = "^\\w*$";
        if (!userRegisterDTO.getUsername().matches(usernameRegex))
            throw new DataValidationException("Username must only contain certain characters, such as letters, numbers, and underscores");

        // RFC5322 standard for EMAIL address
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!userRegisterDTO.getEmail().matches(emailRegex))
            throw new DataValidationException("Email is invalid");
    }

    public void checkPassword(String password) throws DataValidationException {
        // PASSWORD just from 8 to 64 characters
        if (password.length() < 8)
            throw new DataValidationException("Password must be at least 8 characters");
        if (password.length() > 64)
            throw new DataValidationException("Password must be at most 64 characters");
        // password should include a mix of upper and lowercase letters, numbers, and symbols
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";
        if (!password.matches(passwordRegex))
            throw new DataValidationException("Password must include a mix of upper and lowercase letters, numbers, " +
                    "and symbols");
    }
}
