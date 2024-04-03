package fptu.swp391.shoppingcart.user.otp.entities;

import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "OTP_VERIFICATION")
public class OtpVerificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TOKEN", nullable = false)
    private String verificationToken;

    @OneToOne()
    @JoinColumn(name = "user_auth_entity_id")
    private UserAuthEntity userAuthEntity;

}
