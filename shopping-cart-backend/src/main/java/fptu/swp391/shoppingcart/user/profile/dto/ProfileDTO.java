package fptu.swp391.shoppingcart.user.profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDTO {
    private int version;
    private String username;
    private String fullName;
    private String email;
    private boolean isEmailVerified;
    private String phone;
    private boolean isPhoneVerified;
//    private String address;
//    private String province;//    Tỉnh/Thành phố    ->  Province/City
//    private String district;//    Quận/Huyện	    ->  District/County
//    private String ward;    //    Phường/Xã	        ->  Ward/Commune
    private GenderDTO gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday; // format dd/MM/yyyy
    private Integer weight;//   in kg
    private Integer height;//   in cm


}
