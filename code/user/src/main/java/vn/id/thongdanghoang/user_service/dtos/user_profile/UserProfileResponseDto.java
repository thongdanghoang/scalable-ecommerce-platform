package vn.id.thongdanghoang.user_service.dtos.user_profile;

import lombok.Builder;

@Builder
public record UserProfileResponseDto(
        String id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String address) {
}
