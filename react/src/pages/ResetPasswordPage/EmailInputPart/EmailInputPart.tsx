import { useState } from "react";
import "./email-input.css";
import { sendEmailResetPassword } from "../../../services/userService";
import { OTPPart } from "../OTPPart/OTPPart";
export default function EmailInputPart() {

  const handleSubmit = async (email: string) => {
    let response = await sendEmailResetPassword(email);
    if (response != null) {
      response.ok;
      console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
      setCurrentForm(() => {
        return <OTPPart email={email} />;
      });
      console.log(response);
    }
  };

  const EmailForm = () => {
    const [email, setEmail] = useState("");

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      let { name, value } = event.target;
      setEmail(value);
    };
    return (
      <div
        id="email-input"
        className="d-flex align-items-center justify-content-center bg-image"
      >
        <div className="card text-center shadow rounded" id="email-input-form">
          <div className="card-header h5 text-white bg-primary">
            Password Reset
          </div>
          <div className="card-body px-5">
            <p className="card-text py-2">
              Enter your email address and we'll send you an email with
              instructions to reset your password.
            </p>
            <div className="form-outline">
              <input
                type="email"
                id="typeEmail"
                className="form-control my-3 is-invalid"
                name="email"
                value={email}
                onChange={handleChange}
              />
              <label className="form-label" htmlFor="typeEmail">
                Email input
              </label>
            </div>
            <button
              className="btn btn-primary w-100"
              onClick={() => handleSubmit(email)}
            >
              Reset password
            </button>
            <div className="d-flex justify-content-between mt-4">
              <a className="" href="#">
                Login
              </a>
              <a className="" href="#">
                Register
              </a>
            </div>
          </div>
        </div>
      </div>
    );
  };
  let [CurrentForm, setCurrentForm] = useState(() => {
    return <EmailForm />;
  });

  return CurrentForm;
}
