package fptu.swp391.shoppingcart.user.address.model.service.impl;

import fptu.swp391.shoppingcart.user.address.dto.AddressDto;
import fptu.swp391.shoppingcart.user.address.model.entity.AddressEntity;
import fptu.swp391.shoppingcart.user.address.model.mapping.AddressMapper;
import fptu.swp391.shoppingcart.user.address.model.repository.AddressRepository;
import fptu.swp391.shoppingcart.user.address.model.service.AddressService;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.DataValidationException;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.validator.UserValidator;
import fptu.swp391.shoppingcart.user.profile.exceptions.AuthorizationException;
import fptu.swp391.shoppingcart.user.profile.exceptions.ConcurrentUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserValidator validator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper mapper;

    private static void setOthersAddressToNotDefault(AddressEntity addressEntity) {
        addressEntity.setDefault(false);
    }

    @Override
    public void addAddress(AddressDto addressDto, String username) throws DataValidationException {

        validator.checkAddress(addressDto);

        userRepository.findUserByUsername(username).ifPresent(user -> {
            if (addressDto.isDefault()) {
                user.getAddresses().forEach(AddressServiceImpl::setOthersAddressToNotDefault);
            }
            user.getAddresses().add(mapper.toEntity(addressDto));
            userRepository.save(user);
        });
    }

    @Override
    public List<AddressDto> getAllAddress(String username) {
        Optional<UserAuthEntity> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            return mapper.toDTOs(user.get().getAddresses());
        }
        return new ArrayList<>();
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto, String username) throws DataValidationException {
        UserAuthEntity userAuthEntity = validateAuthorization(addressDto, username);
        validator.checkAddress(addressDto);

        AddressEntity oldAddress = addressRepository.findById(addressDto.getId()).orElseThrow();

        if(oldAddress.getVersion() != addressDto.getVersion()) {
            throw new ConcurrentUpdateException("This address has been updated by another user");
        }

        AddressEntity newAddress = mapper.toEntity(addressDto);

        oldAddress.setFullName(newAddress.getFullName());
        oldAddress.setPhone(newAddress.getPhone());
        oldAddress.setType(newAddress.getType());
        oldAddress.setAddressDetail(newAddress.getAddressDetail());
        oldAddress.setWard(newAddress.getWard());
        oldAddress.setDistrict(newAddress.getDistrict());
        oldAddress.setProvince(newAddress.getProvince());
        oldAddress.setDefault(newAddress.isDefault());

        if (oldAddress.isDefault()) {
            userAuthEntity.getAddresses()
                    .stream()
                    .filter(address -> !address.getId().equals(oldAddress.getId()))
                    .forEach(AddressServiceImpl::setOthersAddressToNotDefault);
        }

        addressRepository.save(oldAddress);
        return mapper.toDTO(oldAddress);
    }

    @Override
    public boolean deleteAddress(AddressDto addressDto, String username) throws DataValidationException {
        validateAuthorization(addressDto, username);
        if(addressRepository.findById(addressDto.getId()).orElseThrow().isDefault()){
            throw new DataValidationException("Cannot delete default address");
        }
        userRepository.findUserByUsername(username).ifPresent(user -> {
            user.getAddresses().removeIf(address -> address.getId() == addressDto.getId());
            userRepository.save(user);
        });
        return true;
    }

    private UserAuthEntity validateAuthorization(AddressDto addressDto, String username) {
        UserAuthEntity user = userRepository.findUserByUsername(username).orElseThrow();

        if (user.getAddresses()
                .stream()
                .noneMatch(address -> address.getId() == addressDto.getId())) {
            throw new AuthorizationException("You are not authorized to access this address");
        }

        return user;
    }
}
