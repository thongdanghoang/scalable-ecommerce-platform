package vn.id.thongdanghoang.n3tk.common.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum N3tkRole {

    USER(RoleNameConstant.USER),
    FEDERAL(RoleNameConstant.OWNER),
    FEDERAL_SUPERVISOR(RoleNameConstant.ADMIN);

    private final String roleValue;

    public static N3tkRole fromValue(String value) {
        return Arrays.stream(N3tkRole.values())
                .filter(role -> StringUtils.equals(role.getRoleValue(), value))
                .findAny()
                .orElse(null);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RoleNameConstant {

        /**
         * Base permission for the n3tk core application.
         */
        public static final String BASE_ROLE = "default-roles-n3tk";

        /**
         * Regular user of n3tk.
         */
        public static final String USER = "n3tk.user";

        /**
         * The n3tk core application owner
         */
        public static final String OWNER = "n3tk.owner";

        /**
         * The n3tk core application administrator
         */
        public static final String ADMIN = "n3tk.admin";

    }
}
