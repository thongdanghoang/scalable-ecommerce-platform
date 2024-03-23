import React, { useEffect, useState } from "react";
import "./register.css";
import { useNavigate } from "react-router-dom";
import { registerService } from "../../services/userService";
import { useDispatch, useSelector } from "react-redux";
import { updateUser } from "../../redux/slides/userSlide";
import { RootState } from "../../redux/store";
import { userRegister } from "../../model/UserModal";
import fashion from "../../assets/img/woman-and-man-fashion-models-vector-19842053.jpg";
import logo from "../../assets/img/n3tk-high-resolution-logo-black-transparent.png";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<userRegister>({
    username: "",
    email: "",
    password: "",
    password2: "",
  });
  const user = useSelector((state: RootState) => state.user);
  const dispatch = useDispatch();
  const [error, setError] = useState({
    isInvalid: false,
    msg: "",
  });

  useEffect(() => {
    setError({
      isInvalid: false,
      msg: "",
    })
  },[formData])

  const handleSignUp = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (formData.password !== formData.password2 && formData.password2 !== "") {
      setError({
        isInvalid: true,
        msg: "Confirm password is not matching",
      });
    } else {
      const res = await registerService(formData);
      if (res?.success) {
        navigate("/");
        dispatch(updateUser({ ...user, username: res?.data }));
      } else {
        setError({
          isInvalid: true,
          msg: res?.message,
        });
      }
    }
  };

  return (
    <div id="RegisterPage" className="background " style={{ height: "125vh" }}>
      <div className="container row">
        <div className="image-fashion col-md-6">
          <div className="image-fashion-logo">
            <img src={logo} alt="" />
          </div>
          <div className="image-fashion-fashion">
            <img src={fashion} alt="" />
          </div>
        </div>
        <div className="register-form col-md-6">
          <h1>Đăng kí</h1>
          <form onSubmit={handleSignUp}>
            <div className={`form-control ${error.isInvalid && error?.msg?.includes("Username") ? 'error' : 'success'}`}>
              <input
                required
                type="text"
                name="username"
                value={formData.username}
                id="username"
                placeholder="Tên tài khoản"
                onChange={(e) =>
                  setFormData({ ...formData, [e.target.name]: e.target.value })
                }
              />
              <span></span>
              <small>
                {error?.isInvalid && error?.msg.includes("Username")
                  ? error?.msg
                  : ""}
              </small>
            </div>
            <div className={`form-control ${error.isInvalid && error?.msg?.includes("Email") ? 'error' : 'success'}`}>
              <input
                required
                name="email"
                value={formData.email}
                id="email"
                placeholder="Email"
                onChange={(e) =>
                  setFormData({ ...formData, [e.target.name]: e.target.value })
                }
              />
              <span></span>
              <small>
                {error?.isInvalid && error?.msg.includes("Email")
                  ? error?.msg
                  : ""}
              </small>
            </div>
            <div className={`form-control ${error.isInvalid && error?.msg?.includes("Password") ? 'error' : 'success'}`}>
              <input
                required
                type="password"
                name="password"
                value={formData.password}
                id="password"
                placeholder="Mật khẩu"
                onChange={(e) =>
                  setFormData({ ...formData, [e.target.name]: e.target.value })
                }
              />
              <span></span>
              <small>
                {error?.isInvalid && error?.msg.includes("Password")
                  ? error?.msg
                  : ""}
              </small>
            </div>
            <div className={`form-control ${error.isInvalid && error?.msg?.includes("Confirm password") ? 'error' : 'success'}`}>
              <input
                required
                type="password"
                name="password2"
                value={formData.password2}
                id="password2"
                placeholder="Nhập lại mật khẩu"
                onChange={(e) =>
                  setFormData({ ...formData, [e.target.name]: e.target.value })
                }
              />
              <span></span>
              <small>
                {error?.isInvalid && error?.msg.includes("Confirm password")
                  ? error?.msg
                  : ""}
              </small>
            </div>
            <input type="submit" value="Đăng kí" />
            <div className="signup_link">
              Đã có tài khoản?{" "}
              <h5
                style={{ cursor: "pointer", color: "balck" }}
                onClick={() => navigate("/sign-in")}
              >
                Đăng nhập
              </h5>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
