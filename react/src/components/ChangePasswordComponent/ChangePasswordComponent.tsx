import { useState } from "react";
import { toast } from "react-toastify";
import { changePassword } from "../../services/userService";

export default function ChangePasswordComponent() {
  let [password, setPassword] = useState({
    oldPassword: "",
    newPassword: "",
  });
  let [invalidPasswordMessage, setInvalidPasswordMessage] = useState({
    isInvalid: true,
    invalidMessage: "",
  });
  const handleChangePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setPassword((prev) => {
      let currentPassword = {
        ...prev,
        [name]: value.trim(),
      };
      return currentPassword;
    });
    setInvalidPasswordMessage({
      isInvalid: false,
      invalidMessage: "",
    });
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (password.oldPassword.length == 0 && password.oldPassword.length == 0) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage: "Mật khẩu cũ và mới bị trống",
      });
      return;
    }

    if (password.oldPassword.length == 0) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage: "Mật khẩu cũ bị trống",
      });
      return;
    }
    if (password.newPassword.length == 0) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage: "Mật khẩu mới bị trống",
      });
      return;
    }

    if (password.newPassword.length < 6) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage: "Mật khẩu mới phải chứa ít nhất 6 kí tự",
      });
      return;
    }

    if (password.newPassword.includes(" ")) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage:
          "Mật khẩu mới không hợp lệ (chứa khoảng trắng, dấu cách)",
      });
      return;
    }

    if (password.oldPassword == password.newPassword) {
      setInvalidPasswordMessage({
        isInvalid: true,
        invalidMessage: "Mật khẩu cũ và mật khẩu mới trùng nhau",
      });
      return;
    }

    let response = await changePassword(password);
    if (response && response.status == undefined) {
      setPassword({
        oldPassword: "",
        newPassword: "",
      });
      toast.success("Thay đổi mật khẩu thành công!");
    } else {
      toast.error("Mật khẩu cũ không đúng!");
    }
  };
  return (
    <div id="ChangePasswordComponent">
      <form onSubmit={handleSubmit} className="p-3">
        <div className="mb-3 form-group row">
          <label className="col-sm-2 col-form-label">Mật khẩu cũ: </label>
          <div className="col-sm-10">
            <input
              type="password"
              className="form-control"
              name="oldPassword"
              value={password.oldPassword}
              onChange={handleChangePassword}
            />
          </div>
        </div>
        <div className="mb-3 form-group row">
          <label className="col-md-2 col-form-label">Mật khẩu mới: </label>
          <div className="col-sm-10">
            <input
              type="password"
              className="form-control"
              name="newPassword"
              value={password.newPassword}
              onChange={handleChangePassword}
            />
          </div>
        </div>
        <div className="has-validation d-flex flex-column justify-content-center align-items-center">
          <button
            type="submit"
            className={`btn btn-primary col-sm-4 ${
              invalidPasswordMessage.isInvalid ? "is-invalid" : null
            }`}
          >
            Xác nhận
          </button>
          <div className="invalid-feedback text-center">
            {invalidPasswordMessage.invalidMessage}
          </div>
        </div>
      </form>
    </div>
  );
}
