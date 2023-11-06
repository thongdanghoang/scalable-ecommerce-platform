import { useState } from "react";
import OTPInput from "react-otp-input";
import "./otp.css";
import { ResetPasswordPart } from "../ResetPasswordPart/ResetpasswordPart";
import { forgetPasswordByMail } from "../../../services/userService";

export function OTPPart({ email }: any) {

  const handleAPI = async (otp: string): Promise<string | null> => {
    let response = await forgetPasswordByMail(email, otp);
    if (response != null) {
      console.log(await response.json())
      if (response.status == 200) {
        setCurrentForm(() => {
          return <ResetPasswordPart />;
        });
      } else {
        let errMess = await response.json();
        return errMess.message;
      }
    }
    return null;
  };

  const OTPForm = () => {
    const [otp, setOtp] = useState("");
    const [validateOTP, setValidateOTP] = useState({
      invalidOTP: false,
      errorOTPMessage: "",
    });

    const handleSubmit = async () => {
      let result = await handleAPI(otp);
      if (result != null) {
      setValidateOTP({
        invalidOTP: true,
        errorOTPMessage: result
      })
    }
    };
    return (
      <>
        <div
          id="otp-form"
          className="d-flex align-items-center justify-content-center"
        >
          <div className="container p-3 mb-5 rounded d-flex flex-column align-items-center justify-content-around">
            <div className="text-center">Chúng tôi đã gửi mã OTP đến email của bạn</div>
            <p>Vui lòng kiểm tra email của bạn và nhập mã OTP tại đây</p>
            <OTPInput
              value={otp}
              onChange={(event) => {
                setOtp(event);
                setValidateOTP({
                  invalidOTP: false,
                  errorOTPMessage: ""
                })
              }}
              numInputs={6}
              inputType="number"
              renderSeparator={<span>&nbsp;</span>}
              renderInput={(props) => <input {...props} />}
              containerStyle={"otp-input-container"}
              inputStyle={"otp-input shadow"}
            />
            <div >
              <p className="text-danger">{validateOTP.invalidOTP ? validateOTP.errorOTPMessage: null}</p>
              <button
                className={`btn btn-primary ${
                  otp.length != 6 ? "disabled" : null
                }`}
                onClick={() => handleSubmit()}
              >
                Xác nhận
              </button>
            </div>
          </div>
        </div>
      </>
    );
  };

  let [CurrentForm, setCurrentForm] = useState(() => {
    return <OTPForm />;
  });
  return CurrentForm;
}

