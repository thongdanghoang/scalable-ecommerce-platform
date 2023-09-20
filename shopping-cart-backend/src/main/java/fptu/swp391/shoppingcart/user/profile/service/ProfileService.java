package fptu.swp391.shoppingcart.user.profile.service;

import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.profile.dto.ProfileDTO;
import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import fptu.swp391.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import fptu.swp391.shoppingcart.user.profile.mapping.ProfileMapper;
import fptu.swp391.shoppingcart.user.profile.repository.ProfileRepository;
import fptu.swp391.shoppingcart.user.profile.validator.ProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileValidator validator;

    @Autowired
    private ProfileMapper mapper;

    @PersistenceContext
    private EntityManager em;

    public ProfileDTO getProfile() {
        // Get the currently authenticated user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileDTO retrievedProfileDto = mapper.toDTO(profileRepository.findByUserUsername(username).orElseThrow());
        retrievedProfileDto.setUsername(username);
        return retrievedProfileDto;
    }

    @Transactional(rollbackFor = {ObjectOptimisticLockingFailureException.class, Exception.class})
    public ProfileDTO updateBasic(ProfileDTO profileDTO) throws DataValidationException {
        // basic fields are: fullName, gender, birthday, weight, height
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals(profileDTO.getUsername())) {
            throw new AuthorizationException("You just can update your own profile");
        }

        validator.validateBasicProfileInfo(profileDTO);

        ProfileEntity existsProfile = profileRepository.findByUserUsername(authenticatedUsername).orElseThrow();

        em.detach(existsProfile);

        ProfileEntity updated = mapper.toEntity(profileDTO);

        // set advanced fields
        updated.setId(existsProfile.getId());
        updated.setPhone(existsProfile.getPhone());
        updated.setPhoneVerified(existsProfile.isPhoneVerified());
        updated.setEmail(existsProfile.getEmail());
        updated.setEmailVerified(existsProfile.isEmailVerified());

        try {
            ProfileEntity result = profileRepository.save(updated);
            ProfileDTO resultDto = mapper.toDTO(result);
            resultDto.setUsername(profileDTO.getUsername());
            return resultDto;
        } catch (ObjectOptimisticLockingFailureException oolfe) {
            throw new ConcurrentUpdateException(
                    String.format("User profile with username %s has been updated by another user",
                            profileDTO.getUsername()));
        }
    }
}
