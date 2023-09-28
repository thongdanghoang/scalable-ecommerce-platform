import { useState } from "react";
import "./email-input.css";
// import { sendEmailResetPassword } from "../../../services/userService";
import { OTPPart } from "../OTPPart/OTPPart";
import { sendEmailOTPForgetPassword } from "../../../services/userService";


export default function EmailInputPart() {
  const handleAPI = async (email: string): Promise<string | null> => {
    console.log(email);
    let response = await sendEmailOTPForgetPassword(email);
    if (response != null) {
      if (response.status == undefined) {
        setCurrentForm(() => {
          return <OTPPart email={email} />;
        });
      } else {
        return response.message;
      }
    }
    return null;
  };

  const EmailForm = () => {
    const [email, setEmail] = useState("");

    const [validateEmail, setValidateEmail] = useState({
      invalidEmail: false,
      errorEmailMessage: "",
    });
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      let { name, value } = event.target;
      setEmail(value);
      setValidateEmail({...validateEmail, invalidEmail: false});
    };

    const handleSubmit = async (
      event: React.MouseEvent<HTMLButtonElement, MouseEvent>
    ) => {
      event.preventDefault();
      let result = await handleAPI(email);
      if (result != null) {
        setValidateEmail({ invalidEmail: true, errorEmailMessage: result });
      }
    };
    return (
      <div id="email-form">
        <div className="main">
          <div className="container a-container d-flex flex-column" id="a-container">
            <form className="form">
              <h2 className="form_title title ">Enter your email</h2>
              <div>
                <input
                  value={email}
                  className={`form__input form-control ${
                    validateEmail.invalidEmail ? "is-invalid" : null
                  }`}
                  type="text"
                  placeholder="Email"
                  onChange={handleChange}
                />
                <div className="invalid-feedback">
                  {validateEmail.errorEmailMessage}
                </div>
              </div>
              <button
                className="form__button button"
                onClick={(event) => handleSubmit(event)}
              >
                SEND EMAIL
              </button>
            </form>
            <div>
            <a href="/sign-in" className="m-3">Sign in</a>
            <a href="/register" className="m-3">Resister</a>
            </div>
          </div>
          
        </div>
      </div>
    );
  };

  let [CurrentForm, setCurrentForm] = useState(() => {
    return <OTPPart />;
  });

  return CurrentForm;
}
