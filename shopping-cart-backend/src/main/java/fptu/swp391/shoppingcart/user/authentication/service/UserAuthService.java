package fptu.swp391.shoppingcart.user.authentication.service;

import fptu.swp391.shoppingcart.user.authentication.dto.UserRegisterDTO;
import fptu.swp391.shoppingcart.user.authentication.entity.Authority;
import fptu.swp391.shoppingcart.user.authentication.entity.UserEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.authentication.mapping.UserMapper;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.validator.UserRegistrationValidator;
import fptu.swp391.shoppingcart.user.management.entity.UserProfileEntity;
import fptu.swp391.shoppingcart.user.management.repository.UserProfileRepository;
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

import java.util.Set;

@Service
@Transactional
public class UserAuthService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    public UserRegisterDTO register(UserRegisterDTO userRegisterDTO)
            throws AuthenticationException, DataValidationException {

        userRegistrationValidator.validate(userRegisterDTO);

        // Map to entity and manually set attributes
        UserEntity register = userMapper.toEntity(userRegisterDTO);
        register.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
        if (userRegisterDTO.getAuthorities().contains("ROLE_USER")) { // TODO: adjust this later
            Authority authority = new Authority("ROLE_USER");
            register.setAuthorities(Set.of(authority));
        }

        UserEntity saved;
        try {
            saved = userRepository.save(register);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Username already exists");
        }

        // Setup user profile with email
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUser(saved);
        userProfileEntity.setEmail(userRegisterDTO.getEmail());
        try {
            userProfileRepository.save(userProfileEntity);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Email already exists");
        }

        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(userRegisterDTO.getUsername(), userRegisterDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userMapper.toDTO(saved);
    }
}
