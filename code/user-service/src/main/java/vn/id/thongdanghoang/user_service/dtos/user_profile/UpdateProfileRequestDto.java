package vn.id.thongdanghoang.user_service.dtos.user_profile;

import jakarta.validation.constraints.Size;

import lombok.Builder;

@Builder
public record UpdateProfileRequestDto(
        @Size(max = 100) String firstName,

        @Size(max = 100) String lastName,

        @Size(max = 20) String phoneNumber,

        String address) {
}
