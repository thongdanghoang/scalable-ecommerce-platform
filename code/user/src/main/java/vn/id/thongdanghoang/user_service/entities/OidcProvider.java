package vn.id.thongdanghoang.user_service.entities;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OidcProvider {
    GOOGLE("google"),
    GITHUB("github");

    private final String value;

    public static final String ATTRIBUTE_EMAIL = "email";
    public static final String ATTRIBUTE_GOOGLE_GIVEN_NAME = "given_name";
    public static final String ATTRIBUTE_GOOGLE_FAMILY_NAME = "family_name";
    public static final String ATTRIBUTE_GITHUB_LOCATION = "location";
    public static final String ATTRIBUTE_GITHUB_NAME = "name";
    public static final String ATTRIBUTE_GITHUB_LOGIN = "login";

    public static OidcProvider fromValue(String value) {
        return Arrays.stream(values())
                .filter(provider -> provider.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider: " + value));
    }
}
