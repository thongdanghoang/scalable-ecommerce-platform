import "./login.css";
import logo from "../../assets/img/logo.png";
import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { LogInWithGoogle, loginService } from "../../services/userService";
import {useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {updateUser } from "../../redux/slides/userSlide";
import { RootState } from "../../redux/store";
import {toast , ToastContainer } from "react-toastify";
import {useEffect} from 'react'
import { toastMSGObject } from "../../utils/utils";
import { addOrderToList } from "../../redux/slides/listOrdersSlide";
import { cloneOrder } from "../../redux/slides/orderSlide";

export default function LoginPage() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [showModal, setShowModal] = useState(false);
  const handleClose = () => setShowModal(false);
  const handleShow = () => setShowModal(true);
  const navigate = useNavigate();
  const user = useSelector((state:RootState)=> state.user); console.log(user);
  const order = useSelector((state:RootState)=> state.order);
  const listOrder = useSelector((state:RootState)=> state.listOrder);
  const dispatch = useDispatch();
  const {state : msgAuthen} = useLocation();

  useEffect(() => {
    if(msgAuthen){
      toast.error(msgAuthen, toastMSGObject())
    }
  },[msgAuthen])

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

    console.log(params.toString());

    const response = await loginService(params);
    if (response) {
      console.log(response)
      if (response?.success) {
        dispatch(updateUser({...user, username : response?.data}));
        const findOrderUnpaid = listOrder.ordersUnpaid.find(order => order.username === response?.data)
        if(findOrderUnpaid){
          dispatch(cloneOrder(findOrderUnpaid))
        }else{
          dispatch(addOrderToList({...order , username : response?.data}))
          dispatch(cloneOrder({...order , username : response?.data}))
        }
        navigate('/');
      } else {
        handleShow();
      }
    }
  };

  const LoginModal = () => {
    return (
      <>
        <Modal
          show={showModal}
          onHide={handleClose}
          backdrop="static"
          keyboard={false}
        >
          <Modal.Header closeButton>
            <Modal.Title>Login failed!</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p className="text-danger">Wrong user or password!</p>
            <p>Forget your password?</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
            <Button
              variant="primary"
              onClick={() => window.location.replace("/")}
            >
              Reset password
            </Button>
          </Modal.Footer>
        </Modal>
      </>
    );
  };

  const handleLogInWithGoogle = async () => {
    window.location.href =
    "http://localhost:8080/login/oauth2/code/google/authorize";
  }

  return (
    <div id="LoginPage" className="background h-100">
      <LoginModal />
      <ToastContainer/>
      <div className="container py-5 h-100">
        <div className="row d-flex justify-content-center align-items-center h-100">
          <div className="col-xl-10">
            <div className={`card rounded-3 text-black`}>
              <div className="row g-0" style={{ borderRadius: "20px" }}>
                <div className="col-lg-6">
                  <div className="card-body p-md-5 mx-md-4">
                    <div className="text-center">
                      <img src={logo} style={{ width: "60%" }} alt="logo" />
                      <h4 className="mt-1 mb-5 pb-1"></h4>
                    </div>

                    <form onSubmit={handleSubmit}>
                      <p>Please login to your account</p>

                      <div className="form-outline mb-4">
                        <input
                          type="username"
                          id="form2Example11"
                          className="form-control"
                          name="username"
                          placeholder="User name"
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
                          placeholder="Password"
                          value={formData.password}
                          onChange={handleChange}
                        />
                      </div>

                      <div className={"text-center customBtn pt-1 mb-5 pb-1"}>
                        <button
                          className={`btn btn-primary ${
                            validInput() ? "" : "disabled"
                          }`}
                          type="submit"
                        >
                          Log in
                        </button>
                        <button
                          type="button"
                          style={{background:"#e7e5e5"}}
                          className="btn btn-link"
                          onClick={handleLogInWithGoogle}
                        >
                          <i className="fa-brands fa-google-plus-g"></i>
                          Google
                        </button>
                        <div className="text-muted mt-3">
                          Forgot password?
                        </div>
                      </div>

                      <div className="d-flex align-items-center justify-content-center pb-4">
                        <p className="mb-0 me-2">Don't have an account?</p>
                        <button
                          type="button"
                          className="btn btn-outline-danger"
                          onClick={() => navigate('/sign-up')}
                        >
                          Create new
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
                <div
                  className="col-lg-6 d-flex align-items-center gradient-custom-2"
                  style={{ borderRadius: "15px" }}
                >
                  <div className="text-white px-3 py-4 p-md-5 mx-md-4">
                    <h4 className="mb-4">We are more than just a company</h4>
                    <p className="small mb-0">
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit,
                      sed do eiusmod tempor incididunt ut labore et dolore magna
                      aliqua. Ut enim ad minim veniam, quis nostrud exercitation
                      ullamco laboris nisi ut aliquip ex ea commodo consequat.
                    </p>
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
