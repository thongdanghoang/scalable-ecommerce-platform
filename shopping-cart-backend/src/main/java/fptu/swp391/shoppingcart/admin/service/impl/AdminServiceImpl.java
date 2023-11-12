package fptu.swp391.shoppingcart.admin.service.impl;

import fptu.swp391.shoppingcart.admin.model.dto.UserReqDto;
import fptu.swp391.shoppingcart.admin.model.dto.UserResponseDto;
import fptu.swp391.shoppingcart.admin.model.exception.UserUsernameAlreadyExist;
import fptu.swp391.shoppingcart.admin.model.exception.UserUsernameNotFoundException;
import fptu.swp391.shoppingcart.admin.model.mapping.UserResponseMapper;
import fptu.swp391.shoppingcart.admin.service.AdminService;
import fptu.swp391.shoppingcart.user.authentication.entity.Authority;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.validator.UserValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserValidator userValidator;

    public AdminServiceImpl(UserRepository userRepository,
                            UserResponseMapper userResponseMapper,
                            BCryptPasswordEncoder passwordEncoder,
                            UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @Override
    public UserResponseDto insert(UserReqDto user) throws DataValidationException {
        userValidator.validateUser(user);

        userRepository.findUserByUsername(user.getUsername()).ifPresent(userAuthEntity -> {
            throw new UserUsernameAlreadyExist(String.format("User with username %s already exists", user.getUsername()));
        });
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername(user.getUsername());
        userAuthEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userAuthEntity.setAuthorities(Set.of(new Authority(user.getRole().toString())));
        return userResponseMapper.toDTO(userRepository.save(userAuthEntity));
    }

    @Override
    public UserResponseDto modify(UserReqDto user) throws DataValidationException {
        userValidator.validateUser(user);

        var userAuth = userRepository.findByAuthoritiesNotContainingRoleUserAndUsername(user.getUsername());
        if (userAuth.isEmpty()) {
            throw new UserUsernameNotFoundException(
                    String.format("User with username %s not found to update.", user.getUsername()));
        }

        UserAuthEntity userAuthEntity = userAuth.get();
        if (!StringUtils.isEmpty(user.getPassword())) {
            userValidator.checkPassword(user.getPassword());
            userAuthEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user.getRole() != null){
            Set<Authority> authorities = userAuthEntity.getAuthorities();
            authorities.clear();
            authorities.add(new Authority(user.getRole().toString()));
        }
        if(user.getEnabled() != null){
            userAuthEntity.setEnabled(user.getEnabled());
        }
        UserAuthEntity saved = userRepository.save(userAuthEntity);
        return userResponseMapper.toDTO(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userResponseMapper.toDTOs(userRepository.findAllByAuthoritiesNotContainingRoleUser());
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        Optional<UserAuthEntity> userByUsername = userRepository.findByAuthoritiesNotContainingRoleUserAndUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UserUsernameNotFoundException(String.format("User with username %s not found", username));
        }
        return userResponseMapper.toDTO(userByUsername.get());
    }

    @Override
    public UserResponseDto destroy(String username) {
        Optional<UserAuthEntity> user = userRepository.findByAuthoritiesNotContainingRoleUserAndUsername(username);
        if (user.isEmpty()) {
            throw new UserUsernameNotFoundException(String.format("User with username %s not found", username));
        }
        UserAuthEntity userAuthEntity = user.get();
        userAuthEntity.setEnabled(false);
        return userResponseMapper.toDTO(userRepository.save(userAuthEntity));
    }
}
