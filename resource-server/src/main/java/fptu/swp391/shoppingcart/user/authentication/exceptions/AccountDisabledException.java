package fptu.swp391.shoppingcart.user.authentication.exceptions;


import org.springframework.security.core.AuthenticationException;

public class AccountDisabledException extends AuthenticationException {
    public AccountDisabledException(String message) {
        super(message);
    }
}
