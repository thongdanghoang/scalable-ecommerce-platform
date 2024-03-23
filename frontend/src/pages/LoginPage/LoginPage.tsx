import "./login.css";
import logo from "../../assets/img/n3tk-high-resolution-logo-black-transparent.png";
import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { loginService } from "../../services/userService";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { updateUser } from "../../redux/slides/userSlide";
import { RootState } from "../../redux/store";
import { toast } from "react-toastify";
import { useEffect } from "react";
import { toastMSGObject } from "../../utils/utils";
import imagebackground from "../../assets/img/19235643.jpg";
import { API_URL } from "../../utils/constants";

export default function LoginPage() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  // const [showModal, setShowModal] = useState(false);
  // const handleClose = () => setShowModal(false);
  // const handleShow = () => setShowModal(true);
  const navigate = useNavigate();
  const user = useSelector((state: RootState) => state.user);
  console.log(user);
  const order = useSelector((state: RootState) => state.order);
  const dispatch = useDispatch();
  const { state: msgAuthen } = useLocation();

  // handle show msg unauthorized
  useEffect(() => {
    if (msgAuthen) {
      toast.error(msgAuthen, toastMSGObject());
    }
  }, [msgAuthen]);

  // handle login
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validInput = () => {
    if (formData.username.length != 0 && formData.password.length != 0) {
      return true;
    } else {
      return false;
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const params = new URLSearchParams(formData);
    const response = await loginService(params);
    if (response) {
      if (response?.success) {
        const { username, role } = response?.data;
        dispatch(updateUser({ ...user, username, role }));
        navigate("/");
      } else {
        console.log(response);
        if (response?.status == 403) {
          toast.error("Tài khoản này đã bị vô hiệu hóa!");
        } else {
          toast.error("Tài khoản hoặc mật khẩu không chính xác!");
        }
      }
    }
  };

  const handleLogInWithGoogle = async () => {
    window.location.href = `${API_URL}/login/oauth2/code/google/authorize`;
  };

  return (
    <div id="LoginPage" className="background h-100">
      {/* <LoginModal /> */}
      <div className="container py-5 h-100">
        <div className="row d-flex justify-content-center align-items-center h-100">
          <div className="col-xl-10">
            <div className={`card rounded-3 text-black`}>
              <div className="row g-0" style={{ borderRadius: "20px" }}>
                <div className="col-lg-6">
                  <div className="card-body p-md-5 mx-md-4">
                    <div className="text-center ">
                      <img
                        src={logo}
                        style={{
                          top: "-30px",
                          width: "49%",
                          height: "49%%",
                          position: "relative",
                        }}
                        alt="logo"
                      />
                    </div>

                    <form onSubmit={handleSubmit}>
                      <p>Hãy đăng nhập tài khoản của bạn</p>

                      <div className="form-outline mt-1 mb-4">
                        <input
                          type="username"
                          id="form2Example11"
                          className="form-control"
                          name="username"
                          placeholder="Tên tài khoản"
                          value={formData.username}
                          onChange={handleChange}
                        />
                      </div>

                      <div className="form-outline mb-4">
                        <input
                          type="password"
                          id="form2Example22"
                          className="form-control"
                          name="password"
                          placeholder="Mật khẩu"
                          value={formData.password}
                          onChange={handleChange}
                        />
                      </div>

                      <div className={"text-center customBtn pt-1 mb-3 pb-1"}>
                        <button
                          className={`btn btn-outline-dark ${
                            validInput() ? "" : "disabled"
                          }`}
                          type="submit"
                        >
                          Đăng nhập
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-dark mt-4"
                          onClick={handleLogInWithGoogle}
                        >
                          <i className="fa-brands fa-google-plus-g mx-1"></i>
                          Đăng nhập với Google
                        </button>
                        <div
                          className="text-muted mt-3"
                          style={{ cursor: "pointer" }}
                          onClick={() => navigate("/forgot-password")}
                        >
                          Quên mật khẩu?
                        </div>
                      </div>

                      <div className="d-flex align-items-center justify-content-center pb-1">
                        <p className="mb-0 me-1">
                          Chưa có tài khoản? Tạo tài khoản mới
                        </p>
                        <button
                          type="button"
                          className="btn btn-outline-dark"
                          onClick={() => navigate("/sign-up")}
                        >
                          Tạo mới
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
                <div className="col-lg-6 d-flex align-items-center gradient-custom-2">
                  <div>
                    <img
                      src={imagebackground}
                      alt=""
                      style={{ maxWidth: "500px" }}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
