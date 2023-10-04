package fptu.swp391.shoppingcart.user.otp.entities;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EMAIL_OTP")
public class EmailOtpEntity extends BaseEntity {

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "OTP", length = 6,nullable = false)
    private String otp;

    @Column(name = "EXPIRED_IN", nullable = false)
    private LocalDateTime expiredIn;

    @Column(name = "WRONG_SUBMIT", nullable = false)
    private int wrongSubmit;

    public EmailOtpEntity() {
        this.wrongSubmit = 0;
    }
}
