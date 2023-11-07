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
        currentPassword.errPasswordMessage = "Mật khẩu không được trống";
        return currentPassword;
      }

      if (currentPassword.newPassword.length < 6) {
        currentPassword.isValid = false;
        currentPassword.errPasswordMessage =
          "Mật khẩu ít nhất có 6 kí tự";
        return currentPassword;
      }

      if (currentPassword.newPassword !== currentPassword.repeatPassword) {
        currentPassword.isValid = false;
        currentPassword.errPasswordMessage = "Mật khẩu không khớp";
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
          <Modal.Title>Đặt lại mật khẩu thành công</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Nhấn vào nút bên dưới để quay lại trang đăng nhập
        </Modal.Body>
        <Modal.Footer>
          <a className="btn btn-primary" href="/sign-in">Quay về đăng nhập</a>
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
            <div style={{fontSize:'30px', fontWeight:'700', paddingBottom:"50px"}}>Hãy điền mật khẩu mới</div>
            <div className="is-invalid">
              <label className="text-dark " style={{fontSize:'15px', fontWeight:'500', paddingBottom:"5px"}}>Mật khẩu mới</label>
              <input
                value={password.newPassword}
                className={`form__input form-control ${
                  password.isValid ? null : "is-invalid"
                }`}
                name="newPassword"
                type="password"
                placeholder="Nhập mật khẩu mới"
                onChange={handleChangePassword}
              />
              <br />
              <label className="text-dark"  style={{fontSize:'15px', fontWeight:'500', paddingBottom:"5px"}}>Xác nhận mật khẩu mới</label>
              <input
                value={password.repeatPassword}
                className={`form__input form-control ${
                  password.isValid ? null : "is-invalid"
                }`}
                name="repeatPassword"
                type="password"
                placeholder="Nhập lại mật khẩu mới"
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
              CHẤP NHẬN
            </button>
          </form>
          <div></div>
        </div>
      </div>
    </div>
  );
}
