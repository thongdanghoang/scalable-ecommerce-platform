package vn.id.thongdanghoang.user_service.services;

import vn.id.thongdanghoang.user_service.dtos.user_profile.UpdateProfileRequestDto;
import vn.id.thongdanghoang.user_service.dtos.user_profile.UserProfileResponseDto;
import vn.id.thongdanghoang.user_service.entities.UserProfile;
import vn.id.thongdanghoang.user_service.repositories.UserProfileRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfileResponseDto getProfile(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
    }

    public UserProfileResponseDto updateProfile(UUID userId, UpdateProfileRequestDto request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        if (request.firstName() != null) {
            profile.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            profile.setLastName(request.lastName());
        }
        if (request.phoneNumber() != null) {
            profile.setPhoneNumber(request.phoneNumber());
        }
        if (request.address() != null) {
            profile.setAddress(request.address());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        return toDto(savedProfile);
    }

    private UserProfileResponseDto toDto(UserProfile profile) {
        return UserProfileResponseDto.builder()
                .id(profile.getId() != null ? profile.getId().toString() : null)
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .address(profile.getAddress())
                .build();
    }
}
