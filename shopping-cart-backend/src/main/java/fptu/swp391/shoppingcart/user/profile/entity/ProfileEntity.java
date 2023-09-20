package fptu.swp391.shoppingcart.user.profile.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.profile.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "USER_PROFILE")
public class ProfileEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email; // forgot password and each user has only one unregistered email
    @Column(name = "IS_EMAIL_VERIFIED", nullable = false)
    private boolean isEmailVerified; // default is false in constructor

    @Column(name = "PHONE", unique = true)
    private String phone;
    @Column(name = "IS_PHONE_VERIFIED", nullable = false)
    private boolean isPhoneVerified; // default is false in constructor

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    @Column(name = "WEIGHT")
    private Integer weight;//   in kg
    @Column(name = "HEIGHT")
    private Integer height;//   in cm

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserAuthEntity user;

    public ProfileEntity() {
        this.isEmailVerified = false;
        this.isPhoneVerified = false;
    }


}
