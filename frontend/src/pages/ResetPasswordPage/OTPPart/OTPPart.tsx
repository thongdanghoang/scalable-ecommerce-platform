import { useState } from "react";
import OTPInput from "react-otp-input";
import "./otp.css";
import { ResetPasswordPart } from "../ResetPasswordPart/ResetpasswordPart";
import { forgetPasswordByMail } from "../../../services/userService";
import { toast } from "react-toastify";
import { Modal } from "antd";
import { useNavigate } from "react-router-dom";

export function OTPPart({ email }: any) {
  const handleAPI = async (otp: string) => {
    let response = await forgetPasswordByMail(email, otp);
    if (response != null) {
      if (response.ok) {
        setCurrentForm(() => {
          return <ResetPasswordPart />;
        });
      } else {
        toast.error("OTP không chính xác!");
      }
    }
  };

  const OTPForm = () => {
    const [otp, setOtp] = useState("");
    const [times, setTimes] = useState(1);
    const navigate = useNavigate();

    const handleSubmit = async () => {
      if (times == 6) {
        Modal.error({
          title: "Bạn nhập sai OTP quá 5 lần!",
          content: "Quay lại trang trước.",
          onOk: () => navigate(0),
        });
      }
      setTimes(times + 1);
      await handleAPI(otp);
    };
    return (
      <>
        <div
          id="otp-form"
          className="d-flex align-items-center justify-content-center"
        >
          <div className="container p-3 mb-5 rounded d-flex flex-column align-items-center justify-content-around">
            <div className="text-center">
              Chúng tôi đã gửi mã OTP đến email của bạn
            </div>
            <div className="text-center">
              Chúng tôi đã gửi mã OTP đến email của bạn
            </div>
            <p>Vui lòng kiểm tra email của bạn và nhập mã OTP tại đây</p>
            <OTPInput
              value={otp}
              onChange={(event) => {
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
