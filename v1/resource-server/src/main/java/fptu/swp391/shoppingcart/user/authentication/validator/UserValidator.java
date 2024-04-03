package fptu.swp391.shoppingcart.user.authentication.validator;

import fptu.swp391.shoppingcart.admin.model.dto.UserReqDto;
import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserValidator {
    private static void validateUsername(String username) throws DataValidationException {
        // USERNAME from 6 to 20 characters
        if (username.length() < 6)
            throw new DataValidationException("Username must be at least 6 characters");
        if (username.length() > 20)
            throw new DataValidationException("Username must be at most 20 characters");
        // Usernames must only contain certain characters, such as letters, numbers, and underscores.
        String usernameRegex = "^\\w*$";
        if (!username.matches(usernameRegex))
            throw new DataValidationException("Username must only contain certain characters, such as letters, numbers, and underscores");
    }

    public void validateUser(UserReqDto user) throws DataValidationException {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new DataValidationException("Username must not be empty");
        }
    }

    public void validateRegisterDto(UserRegisterDTO userRegisterDTO) throws DataValidationException {
        // empty check
        if (userRegisterDTO.getUsername().isEmpty() || userRegisterDTO.getPassword().isEmpty() || userRegisterDTO.getEmail().isEmpty())
            throw new DataValidationException("Username, password and email must not be empty");

        checkPassword(userRegisterDTO.getPassword());

        validateUsername(userRegisterDTO.getUsername());

        // RFC5322 standard for EMAIL address
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!userRegisterDTO.getEmail().matches(emailRegex))
            throw new DataValidationException("Email is invalid");
    }

    public void checkMail(String mail) throws DataValidationException {
        if (mail == null) {
            throw new DataValidationException("Email must not be empty");
        }
        // RFC5322 standard for EMAIL address
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!mail.matches(emailRegex))
            throw new DataValidationException("Email is invalid");
    }

    public void checkPassword(String password) throws DataValidationException {
        // PASSWORD just from 6 to 64 characters
        if (password.length() < 6)
            throw new DataValidationException("Password must be at least 6 characters");
        if (password.length() > 64)
            throw new DataValidationException("Password must be at most 64 characters");
        // any combination of uppercase letters, lowercase letters, digits, or the specified special characters
        String passwordRegex = "^[A-Za-z\\d!@#$%^&*()-_+=]{6,}$";
        if (!password.matches(passwordRegex))
            throw new DataValidationException("Password is invalid");
    }

    public void checkPhoneNumberE164(String phone) throws DataValidationException {
        if (phone == null) {
            throw new DataValidationException("Phone number must not be empty");
        }
        // PHONE NUMBER must be in E.164 format
        String phoneRegex = "^\\+[1-9]\\d{1,14}$";
        if (!phone.matches(phoneRegex))
            throw new DataValidationException("Phone number is invalid");
    }

    public void checkAddress(AddressDto address) throws DataValidationException {
        checkPhoneNumberE164(address.getPhone());
        if (address.getFullName() == null || address.getFullName().isEmpty()) {
            throw new DataValidationException("Full name must not be empty");
        }
        if (address.getAddressDetail() == null || address.getAddressDetail().isEmpty()) {
            throw new DataValidationException("Address detail must not be empty");
        }
        if (address.getProvince() == null || address.getProvince().isEmpty()) {
            throw new DataValidationException("City must not be empty");
        }
        if (address.getDistrict() == null || address.getDistrict().isEmpty()) {
            throw new DataValidationException("District must not be empty");
        }
        if (address.getWard() == null || address.getWard().isEmpty()) {
            throw new DataValidationException("Ward must not be empty");
        }
    }
}
