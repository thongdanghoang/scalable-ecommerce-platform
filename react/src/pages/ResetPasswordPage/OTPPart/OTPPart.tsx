import { useState } from "react";
import OTPInput from "react-otp-input";
import "./otp.css";
// import { sendEmailResetPassword } from "../../../services/userService";
import { ResetPasswordPart } from "../ResetPasswordPart/ResetpasswordPart";
export function OTPPart({ email }: any) {
  

  const handleSubmit = async (otp: string) => {
console.log(otp)
    // console.log(email);
    // let response = await sendEmailResetPassword("");
    // if (response != null) {
    //   response.ok;
    //   setCurrentForm(() => {
    //     return <ResetPasswordPart />;
    //   });
    //   console.log(response);
    // }
  };

  const OTPForm = () => {
    const [otp, setOtp] = useState("");
    const [validateOTP, setValidateOTP] = useState({

      invalidOTP: false,
      errorOTPMessage: "",
    });
    return (
    <>
      <div
        id="otp-form"
        className="d-flex align-items-center justify-content-center"
      >
        <div className="container shadow p-3 mb-5 rounded d-flex flex-column align-items-center justify-content-around">
          <h3 className="text-center">We have sent OTP code to your email</h3>
          <p>Please check email and enter otp code here</p>
          <OTPInput
            value={otp}
            onChange={(event) => {
              // console.log(event);
              setOtp(event);
            }}
            numInputs={6}
            inputType="number"
            renderSeparator={<span>&nbsp;</span>}
            renderInput={(props) => <input {...props} />}
            containerStyle={"otp-input-container"}
            inputStyle={"otp-input shadow"}
          />
          <div>
            <button className={`btn btn-primary ${otp.length != 6 ? 'disabled': null}`} onClick={() => handleSubmit(otp)}>Submit</button>
          </div>
        </div>
      </div>
    </>
  )};

  let [CurrentForm, setCurrentForm] = useState(() => {
    return <OTPForm/>;
  });
  return CurrentForm;
}
