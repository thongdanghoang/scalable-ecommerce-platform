package fptu.swp391.shoppingcart.user.authentication.entity;

import fptu.swp391.shoppingcart.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EMAIL_OTP")
public class EmailOtpEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserAuthEntity user;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "OTP", nullable = false, length = 6)
    private String otp;

    @Column(name = "CREATED_TIME", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "NEXT_REQUEST_TIME")
    private LocalDateTime nextRequestTime;

    @Column(name = "TOKEN", length = 128)
    private String token; // generated after verify otp

    @Column(name = "WRONG_SUBMIT", nullable = false)
    private int wrongSubmit; // max 3 times

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OtpStatus status;

}
