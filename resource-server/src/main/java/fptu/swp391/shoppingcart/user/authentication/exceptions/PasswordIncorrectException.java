package fptu.swp391.shoppingcart.user.authentication.exceptions;

public class PasswordIncorrectException extends RuntimeException{

        public PasswordIncorrectException() {
            super();
        }

        public PasswordIncorrectException(String message) {
            super(message);
        }

        public PasswordIncorrectException(String message, Throwable cause) {
            super(message, cause);
        }

        public PasswordIncorrectException(Throwable cause) {
            super(cause);
        }
}
