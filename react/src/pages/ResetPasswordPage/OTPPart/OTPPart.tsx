import { useState } from "react";
import OTPInput from "react-otp-input";
import "./otp.css";
import { sendEmailResetPassword } from "../../../services/userService";
import { ResetPasswordPart } from "../ResetPasswordPart/ResetpasswordPart";
export function OTPPart({ email }: any) {
  

  const handleSubmit = async (email: string) => {
    console.log(email);
    let response = await sendEmailResetPassword("");
    if (response != null) {
      response.ok;
      setCurrentForm(() => {
        return <ResetPasswordPart />;
      });
      console.log(response);
    }
  };

  const OTPForm = () => {
    const [otp, setOtp] = useState("");

    return (
    <>
      <div
        id="otp-form"
        className="d-flex align-items-center justify-content-center"
      >
        <div className="container shadow p-3 mb-5 rounded d-flex flex-column align-items-center justify-content-around">
          <h4 className="text-center">We have sent OTP code to your email</h4>
          <p>Please check email and enter otp code here</p>
          <OTPInput
            value={otp}
            onChange={(event) => {
              // console.log(event);
              setOtp(event);
            }}
            numInputs={6}
            inputType="number"
            renderSeparator={<span>-</span>}
            renderInput={(props) => <input {...props} />}
            containerStyle={"otp-input-container"}
            inputStyle={"otp-input"}

            //   borderTop: "none",
            //   borderLeft: "none",
            //   borderRight: "none",
            //   backgroundColor: "transparent",
            //   outline: "none",

            // }}
          />
          <div>
            <button className="btn btn-primary" onClick={() => handleSubmit(otp)}>Submit</button>
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
