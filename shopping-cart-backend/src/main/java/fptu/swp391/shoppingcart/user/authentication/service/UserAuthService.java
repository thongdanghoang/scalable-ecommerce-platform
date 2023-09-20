package fptu.swp391.shoppingcart.user.authentication.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.entity.Authority;
import fptu.swp391.shoppingcart.user.authentication.entity.EmailOtpEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.OtpStatus;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailNotFound;
import fptu.swp391.shoppingcart.user.authentication.exceptions.EmailServiceException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.authentication.exceptions.otp.*;
import fptu.swp391.shoppingcart.user.authentication.mapping.UserMapper;
import fptu.swp391.shoppingcart.user.authentication.repository.EmailOtpRepository;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.validator.UserRegistrationValidator;
import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import fptu.swp391.shoppingcart.user.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Transactional
public class UserAuthService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    @Autowired
    private MailOtpService mailOtpService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    public UserRegisterDTO register(UserRegisterDTO userRegisterDTO)
            throws AuthenticationException, DataValidationException {

        userRegistrationValidator.validate(userRegisterDTO);

        // Map to entity and manually set attributes
        UserAuthEntity register = userMapper.toEntity(userRegisterDTO);
        register.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
        if (userRegisterDTO.getAuthorities().contains("ROLE_USER")) { // TODO: adjust this later
            Authority authority = new Authority("ROLE_USER");
            register.setAuthorities(Set.of(authority));
        }

        UserAuthEntity saved;
        try {
            saved = userRepository.save(register);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Username already exists");
        }

        // Setup user profile with email
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(userRegisterDTO.getEmail());
        profileEntity.setUser(saved);

        try {
            profileRepository.save(profileEntity);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Email already exists");
        }

        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(userRegisterDTO.getUsername(), userRegisterDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userMapper.toDTO(saved);
    }

    public String sendOtp(String email) throws OtpMaxAttemptsExceededException, OtpStillActiveException, OtpVerifiedException {
        UserAuthEntity user = userRepository.findByProfileEmail(email)
                .orElseThrow(() -> new EmailNotFound("Email not found"));

        Optional<EmailOtpEntity> found = emailOtpRepository.findByEmail(email);
        EmailOtpEntity emailOtpEntity;

        if (found.isPresent()) {
            emailOtpEntity = found.get();
            if (emailOtpEntity.getStatus() == OtpStatus.ACTIVE) {
                throw new OtpStillActiveException("OTP is still active. Please check your email");
            }
            if (emailOtpEntity.getStatus() == OtpStatus.VERIFIED) {
                throw new OtpVerifiedException("OTP is verified. Please login");
            }
            if (emailOtpEntity.getStatus() == OtpStatus.WRONG_SUBMIT) {
                if (emailOtpEntity.getNextRequestTime().isAfter(java.time.LocalDateTime.now())) {
                    throw new OtpMaxAttemptsExceededException("OTP was wrong more than 3 times. Please wait 10 minutes and resend OTP");
                } else { // time's out
                    emailOtpEntity.setNextRequestTime(null);
                }
            }
            if (emailOtpEntity.getStatus() == OtpStatus.EXPIRED) {
                emailOtpEntity.setNextRequestTime(null);
                emailOtpEntity.setToken(null);
            }
        } else {
            emailOtpEntity = new EmailOtpEntity();
            emailOtpEntity.setUser(user);
            emailOtpEntity.setEmail(email);
        }
        emailOtpEntity.setOtp(generateOtp());
        emailOtpEntity.setCreateTime(java.time.LocalDateTime.now());
        emailOtpEntity.setWrongSubmit(0);
        emailOtpEntity.setStatus(OtpStatus.ACTIVE);
        emailOtpRepository.save(emailOtpEntity);
        try {
            mailOtpService.sendOtpTo(email, emailOtpEntity.getOtp());
            return "OTP sent successfully to " + email + ". Please check your email.";
        } catch (UnirestException | IOException e) {
            throw new EmailServiceException(e);
        }
    }

    public String verifyOtp(String email, String otp)
            throws OtpIncorrectException, OtpExpiredException, OtpMaxAttemptsExceededException, OtpVerifiedException {
        EmailOtpEntity emailOtpEntity = emailOtpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP is not found or expired"));

        if (emailOtpEntity.getStatus() == OtpStatus.VERIFIED) {
            throw new OtpVerifiedException("OTP already been verified.");
        }

        if (emailOtpEntity.getStatus() == OtpStatus.EXPIRED) {
            throw new OtpExpiredException("OTP is expired. Please request new OTP");
        }

        if (emailOtpEntity.getStatus() == OtpStatus.WRONG_SUBMIT) {
            String message = "OTP is wrong more than 3 times. " +
                    "Please wait 10 minutes and request other OTP";
            if (emailOtpEntity.getNextRequestTime().isBefore(LocalDateTime.now())) { // time's up
                message = "OTP is wrong more than 3 times. Please request other OTP";
            }
            throw new OtpMaxAttemptsExceededException(message);
        }

        // in ACTIVE status below
        if (emailOtpEntity.getWrongSubmit() >= 3) { // too many wrong submit
            emailOtpEntity.setStatus(OtpStatus.WRONG_SUBMIT);
            emailOtpEntity.setNextRequestTime(java.time.LocalDateTime.now().plusMinutes(10));
            emailOtpRepository.save(emailOtpEntity);
            throw new OtpMaxAttemptsExceededException("OTP is wrong more than 3 times. " +
                    "Please wait 10 minutes and request other OTP");
        }

        // wrong otp
        if (!emailOtpEntity.getOtp().equals(otp)) {
            emailOtpEntity.setWrongSubmit(emailOtpEntity.getWrongSubmit() + 1);
            emailOtpRepository.save(emailOtpEntity);
            throw new OtpIncorrectException("OTP incorrect");
        }


        // expired
        if (emailOtpEntity.getCreateTime().plusMinutes(10).isBefore(java.time.LocalDateTime.now())) {
            emailOtpEntity.setStatus(OtpStatus.EXPIRED);
            emailOtpRepository.save(emailOtpEntity);
            throw new OtpExpiredException("OTP is expired. Please request new OTP");
        }

        emailOtpEntity.setStatus(OtpStatus.VERIFIED);
        String secret = generateRandomSecret(64);
        emailOtpEntity.setToken(secret);
        emailOtpEntity.setCreateTime(java.time.LocalDateTime.now());
        emailOtpRepository.save(emailOtpEntity);
        return secret;
    }

    public String resetPassword(String token, String newPassword) {
        EmailOtpEntity otp = emailOtpRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Bad credentials"));
        otp.getUser().setPassword(bCryptPasswordEncoder.encode(newPassword));
        emailOtpRepository.deleteById(otp.getId());
        return "Password reset successfully";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    private String generateRandomSecret(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}
