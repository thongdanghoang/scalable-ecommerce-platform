import { useState } from "react";
import "./reset-password.css";
import { resetPassword } from "../../../services/userService";
import { Button, Modal } from "react-bootstrap";

export function ResetPasswordPart() {
  const [password, setPassword] = useState({
    newPassword: "",
    repeatPassword: "",
    isValid: false,
    errPasswordMessage: "",
  });
  const [showModal, setShowModal] = useState(false);

  const handleCloseModal = () => setShowModal(false);
  const handleShowModal = () => setShowModal(true);



  const handleChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setPassword((previousState) => {
      let currentPassword = {
        ...previousState,
        isValid: true,
        [name]: value.trim(),
      };

      if (
        currentPassword.newPassword.length +
          currentPassword.repeatPassword.length ==
        0
      ) {
        currentPassword.isValid = false;
        currentPassword.errPasswordMessage = "Password is empty";
        return currentPassword;
      }

      if (currentPassword.newPassword.length < 6) {
        currentPassword.isValid = false;
        currentPassword.errPasswordMessage =
          "Password must have at least 6 characters";
        return currentPassword;
      }

      if (currentPassword.newPassword !== currentPassword.repeatPassword) {
        currentPassword.isValid = false;
        currentPassword.errPasswordMessage = "Password not match";
        return currentPassword;
      }

      return currentPassword;
    });
  };

  const handleSubmit = async (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event.preventDefault();
    console.log(password.newPassword)
    let response = await resetPassword(password.newPassword);
    if (response != null) {
      if (response.status == 200) {
        console.log(await response.json())
        handleShowModal();
      } else {
        console.log(await response.json())
      }
    }
  };



  const ModalResetPassword = () => {

    return (<>

      <Modal
        show={showModal}
        onHide={handleCloseModal}
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header>
          <Modal.Title>Reset password successfully!</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Click button below to return sign in page
        </Modal.Body>
        <Modal.Footer>
          <a className="btn btn-primary" href="/sign-in">Return sign in</a>
        </Modal.Footer>
      </Modal>
    </>)

  }

  return (
    <div id="reset-password-form">
      <ModalResetPassword/>
      <div className="main">
        <div
          className="container a-container d-flex flex-column"
          id="a-container"
        >
          <form className="form">
            <h2 className="form_title title ">Enter your new password</h2>
            <div className="is-invalid">
              <label className="text-dark">New password</label>
              <input
                value={password.newPassword}
                className={`form__input form-control ${
                  password.isValid ? null : "is-invalid"
                }`}
                name="newPassword"
                type="password"
                placeholder="New password"
                onChange={handleChangePassword}
              />
              <br />
              <label className="text-dark">Repeat password</label>
              <input
                value={password.repeatPassword}
                className={`form__input form-control ${
                  password.isValid ? null : "is-invalid"
                }`}
                name="repeatPassword"
                type="password"
                placeholder="Repeat password"
                onChange={handleChangePassword}
              />
              <p>{password.isValid ? "" : password.errPasswordMessage}</p>
            </div>
            <button
              className={`form__button button btn btn-primary ${
                password.isValid ? null : "disabled"
              }`}
              onClick={(event) => handleSubmit(event)}
            >
              SEND EMAIL
            </button>
          </form>
          <div></div>
        </div>
      </div>
    </div>
  );
}
