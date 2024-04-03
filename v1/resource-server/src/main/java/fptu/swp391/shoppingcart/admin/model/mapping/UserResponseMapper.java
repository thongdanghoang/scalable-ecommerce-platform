package fptu.swp391.shoppingcart.admin.model.mapping;

import fptu.swp391.shoppingcart.IMapper;
import fptu.swp391.shoppingcart.admin.model.dto.UserResponseDto;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserResponseMapper implements IMapper<UserAuthEntity, UserResponseDto> {
    @Override
    @Mapping(source = "createdDate", target = "createdAt")
    @Mapping(source = "updatedDate", target = "updatedAt")
    @Mapping(target = "role", expression = "java(mapRole(userAuthEntity))")
    public abstract UserResponseDto toDTO(UserAuthEntity userAuthEntity);

    protected String mapRole(UserAuthEntity userAuthEntity) {
        return userAuthEntity.getAuthorities().stream().findFirst().get().getName();
    }
}
