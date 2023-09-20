package fptu.swp391.shoppingcart.user.authentication.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import fptu.swp391.shoppingcart.user.address.entity.AddressEntity;
import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "USER")
public class UserAuthEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NUMBER_OF_FAILED_LOGIN_ATTEMPTS", nullable = false)
    private int numberOfFailedLoginAttempts;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "DISABLED_UNTIL")
    private LocalDateTime disabledUntil;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private Set<Authority> authorities = new LinkedHashSet<>();

    // one user has many addresses
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private Set<AddressEntity> addresses = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileEntity profile;


    //TODO: one user has many payment methods


    public UserAuthEntity() {
        this.numberOfFailedLoginAttempts = 0;
        this.enabled = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAuthEntity that = (UserAuthEntity) o;

        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        return Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        return result;
    }
}
