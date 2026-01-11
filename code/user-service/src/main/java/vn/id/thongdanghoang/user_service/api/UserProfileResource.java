package vn.id.thongdanghoang.user_service.api;

import vn.id.thongdanghoang.user_service.dtos.user_profile.UpdateProfileRequestDto;
import vn.id.thongdanghoang.user_service.dtos.user_profile.UserProfileResponseDto;
import vn.id.thongdanghoang.user_service.securities.UserContextData;
import vn.id.thongdanghoang.user_service.services.UserProfileService;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users/me/profile")
@RequiredArgsConstructor
public class UserProfileResource {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileResponseDto> getProfile(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(userProfileService.getProfile(userId));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequestDto request) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(userProfileService.updateProfile(userId, request));
    }

    private UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        var principal = authentication.getPrincipal();
        if (!(principal instanceof UserContextData currentUser)) {
            throw new IllegalArgumentException("Unsupported authentication principal type");
        }
        return currentUser.getUser().getId();
    }
}
