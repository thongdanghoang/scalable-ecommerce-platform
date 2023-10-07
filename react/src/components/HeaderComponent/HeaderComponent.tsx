import "./header.css";
import { OverlayTrigger, Popover } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { RootState } from "../../redux/store";
import { logoutService } from "../../services/userService";
import { resetUser } from "../../redux/slides/userSlide";
import { useEffect, useState } from "react";

interface propsHeader {
  isShowMenu ?: boolean,
  isShowSearch ?: boolean,
  isShowCart ?: boolean
}

export default function HeaderComponent({isShowMenu = true , isShowSearch = true , isShowCart = true} : propsHeader) {
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
    <Popover onMouseLeave={() => setIsOpenPopover(false)} id="avatar-popover">
      <Popover.Body className="custome-card-popover-body ">
        {
          <>
            <div className="header-card-product">
              <div className="header-card-image-product">
                <a href="">
                  <img
                    src="https://bizweb.dktcdn.net/thumb/compact/100/438/408/products/qam3127-nau-2-1796c057-5e70-467f-aaf7-5290b1b6fbb1.jpg"
                    style={{ width: "83px", height: "124px" }}
                    alt=""
                  />
                </a>
              </div>
              <div className="header-card-content-product">
                <div className="header-card-product-info">
                  <div className="header-card-product-info-detail">
                    <a href="">
                      Quần Âu Nam Cao Cấp Giữ Phom, Co Giãn Thoải Mái
                    </a>

                    <div className="card-product-price">
                      <span>998.000đ</span>
                    </div>

                    <div className="card-product-color">
                      <span>Nâu/L</span>
                    </div>
                  </div>
                  <div className="product-delete-button">
                    <i className="fa-solid fa-trash-can"></i>
                  </div>
                </div>
                <div className="header-card-product-select">
                  <div className="product-select-btn">
                    <button className="button">-</button>
                    <input
                      className="input"
                      value={"1"}
                      placeholder=""
                      type="text"
                      style={{ textAlign: "center" }}
                    />
                    <button className="button">+</button>
                  </div>
                  <div className="product-select-total">
                    <h6>
                      Tổng cộng :<span className="highlight">998.000đ</span>
                    </h6>
                  </div>
                </div>
              </div>
            </div>
            <div className="addToCard-button">
              <button>Thêm vào giỏ hàng</button>
            </div>
          </>
        }
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
                      <i
                        className="header-search-btn-icon fa-solid fa-magnifying-glass"
                      ></i>
                    </button>
                  </div>
                )}
              </div>
              <div className="col-md-5">
                <div className="header-components">
                  <div onClick={() => navigate("/")} className="header-home">
                    <i className="header-cart-icon fa-solid fa-house"></i>
                  </div>
                  <div className="header-account">
                    <OverlayTrigger
                      placement="bottom-end" // Vị trí hiển thị popover ("top", "bottom", "left", "right", vv.)
                      overlay={MyAccountPopover}
                      show={isOpenPopover}
                    >
                      <div
                        className="header-account__content"
                        onMouseEnter={() => setIsOpenPopover(true)}
                      >
                        <i className="header-cart-icon fa-solid fa-user"></i>
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
                        <div className="header-account__content">
                          <i className="header-cart-icon fa-solid fa-cart-shopping"></i>
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
