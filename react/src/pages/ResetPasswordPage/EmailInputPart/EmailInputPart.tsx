import { useState } from "react";
import "./email-input.css";
import { OTPPart } from "../OTPPart/OTPPart";
import { forgetPasswordByMail } from "../../../services/userService";


export default function EmailInputPart() {

  const handleAPI = async (email: string): Promise<string | null> => {
    let response = await forgetPasswordByMail(email);
    if (response != null) {
      if (response.status == 200) {
        setCurrentForm(() => {
          return <OTPPart email={email} />;
        });
      } else {
        let errMess = await response.json();
        return errMess.message;
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
            <a href="/sign-up" className="m-3">Resister</a>
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

