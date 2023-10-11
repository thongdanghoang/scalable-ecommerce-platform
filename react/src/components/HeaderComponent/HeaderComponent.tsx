import "./header.css";
import { OverlayTrigger, Popover } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { RootState } from "../../redux/store";
import { logoutService } from "../../services/userService";
import { resetUser } from "../../redux/slides/userSlide";
import { useEffect, useState } from "react";
import CardHeaderComponent from "../CardHeaderComponent/CardHeaderComponent";
import { Badge } from "antd";

interface propsHeader {
  isShowMenu?: boolean;
  isShowSearch?: boolean;
  isShowCart?: boolean;
}

export default function HeaderComponent({
  isShowMenu = true,
  isShowSearch = true,
  isShowCart = true,
}: propsHeader) {
  const navigate = useNavigate();
  const user = useSelector((state: RootState) => state.user);
  console.log(user);
  const dispatch = useDispatch();
  const [isOpenPopover, setIsOpenPopover] = useState(false);
  const location = useLocation();

  useEffect(() => {
    setIsOpenPopover(false);
  }, [location.pathname]);

  const handleProfile = async () => {
    navigate("/profile-user/information-user");
  };

  const handleLogout = async () => {
    const res = await logoutService();
    if (res?.status == 401) {
      navigate("/sign-in");
    }
    dispatch(resetUser());
  };

  const MyAccountPopover = (
    <Popover onMouseLeave={() => setIsOpenPopover(false)} id="avatar-popover">
      <Popover.Body className="custome-popover-body">
        {user?.username ? (
          <div>
            <div className="menu-item" onClick={handleProfile}>
              User Profile
            </div>
            <div className="menu-item" onClick={handleLogout}>
              Logout
            </div>
          </div>
        ) : (
          <div>
            <div className="menu-item" onClick={() => navigate("/sign-in")}>
              Login
            </div>
            <div className="menu-item" onClick={() => navigate("/sign-up")}>
              Register
            </div>
          </div>
        )}
      </Popover.Body>
    </Popover>
  );
  const MyCardPopover = (
    <Popover onMouseLeave={() => setIsOpenPopover(false)} id="card-popover">
      <Popover.Body className="custome-card-popover-body ">
        <CardHeaderComponent/>
      </Popover.Body>
    </Popover>
  );

  return (
    <div id="header">
      <div className="container">
        <div className="row">
          <div className="col-md-6">
            <div className="row">
              <div className="col-md-3 header_logo">TTNTK</div>
              <div className="col-md-2 header_item text-center">
                <a href="">MEN</a>
              </div>
              <div className="col-md-2 header_item text-center">
                <a href="">WOMEN</a>
              </div>
              <div className="col-md-3 header_item text-center">
                <a href="">BEST SELLER</a>
              </div>
              <div className="col-md-2 header_item text-center"></div>
            </div>
          </div>
          <div className="col-md-6">
            <div className="row">
              <div className="col-md-7">
                {isShowSearch && (
                  <div className="header_search">
                    <input
                      type="text"
                      className="header-search_input"
                      placeholder="Nhập để tìm kiếm sản phẩm "
                    />
                    <button className="header-search-btn">
                      <i className="header-search-btn-icon fa-solid fa-magnifying-glass"></i>
                    </button>
                  </div>
                )}
              </div>
              <div className="col-md-5">
                <div className="header-components">
                  <div 
                    onClick={() => navigate("/")} 
                    className="header-icon"
                  >
                    <i className="fa-solid fa-house"></i>
                  </div>
                  <div className="header-account">
                    <OverlayTrigger
                      placement="bottom-end" // Vị trí hiển thị popover ("top", "bottom", "left", "right", vv.)
                      overlay={MyAccountPopover}
                      show={isOpenPopover}
                    >
                      <div
                        className="header-icon"
                        onMouseEnter={() => setIsOpenPopover(true)}
                      >
                        <i className="fa-solid fa-user"></i>
                      </div>
                    </OverlayTrigger>
                  </div>
                  {isShowCart && (
                    <div className="header-cart">
                      <OverlayTrigger
                        placement="bottom-end" // Vị trí hiển thị popover ("top", "bottom", "left", "right", vv.)
                        overlay={MyCardPopover}
                        show={true}
                      >                       
                        <div className="header-icon">
                          <Badge count={2}>
                            <i className="fa-solid fa-cart-shopping"></i>
                          </Badge>
                        </div>
                      </OverlayTrigger>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
