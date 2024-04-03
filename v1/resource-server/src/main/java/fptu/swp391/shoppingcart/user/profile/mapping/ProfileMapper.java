package fptu.swp391.shoppingcart.user.profile.mapping;

import fptu.swp391.shoppingcart.IMapper;
import fptu.swp391.shoppingcart.user.profile.dto.GenderDTO;
import fptu.swp391.shoppingcart.user.profile.dto.ProfileDTO;
import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import fptu.swp391.shoppingcart.user.profile.entity.enums.Gender;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static fptu.swp391.shoppingcart.user.profile.entity.enums.Gender.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class ProfileMapper implements IMapper<ProfileEntity, ProfileDTO> {
    @Override
    @InheritInverseConfiguration
    @Mapping(target = "gender", qualifiedByName = "mapToGenderEntity")
    @Mapping(target = "phoneVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "email", ignore = true)
    public abstract ProfileEntity toEntity(ProfileDTO profileDTO);

    @Override
    public List<ProfileEntity> toEntities(List<ProfileDTO> profileDTOS) {
        return profileDTOS.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "gender", qualifiedByName = "mapToGenderDTO")
    public abstract ProfileDTO toDTO(ProfileEntity profileEntity);

    @Override
    public List<ProfileDTO> toDTOs(List<ProfileEntity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Named("mapToGenderDTO")
    protected GenderDTO mapToGenderDTO(Gender gender) {
        return (gender != null) ? toGenderDTO(gender) : null;
    }

    @Named("mapToGenderEntity")
    protected Gender mapToGenderEntity(GenderDTO genderDTO) {
        return (genderDTO != null) ? toGenderEntity(genderDTO) : null;
    }

    protected Gender toGenderEntity(GenderDTO genderDTO) {
        switch (genderDTO) {
            case MALE:
                return MALE;
            case FEMALE:
                return FEMALE;
            case OTHER:
                return OTHER;
            default:
                throw new IllegalArgumentException("Invalid GenderDTO value: " + genderDTO);
        }
    }

    protected GenderDTO toGenderDTO(Gender gender) {
        switch (gender) {
            case MALE:
                return GenderDTO.MALE;
            case FEMALE:
                return GenderDTO.FEMALE;
            case OTHER:
                return GenderDTO.OTHER;
            default:
                throw new IllegalStateException("Unmapped Gender enum: " + this);
        }
    }

}
