import "./header.css";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { RootState } from "../../redux/store";
import { logoutService } from "../../services/userService";
import { resetUser } from "../../redux/slides/userSlide";
import { useEffect } from "react";
import CardHeaderComponent from "../CardHeaderComponent/CardHeaderComponent";
import { Badge, Popover } from "antd";
import {
  CartContext,
  CartContextType,
} from "../DefaultComponent/DefaultComponent";
import { useContext } from "react";
import { AiOutlineGroup, AiOutlineProfile } from "react-icons/ai";
import { BiLogOut } from "react-icons/bi";
import { resetOrder } from "../../redux/slides/orderSlide";
import { updateListOrder } from "../../redux/slides/listOrdersSlide";
import SearchBarComponent from "../SearchBarComponent/SearchBarComponent";

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
  const order = useSelector((state: RootState) => state.order);
  console.log(order);
  const listOrder = useSelector((state: RootState) => state.listOrder);
  console.log(listOrder);
  const dispatch = useDispatch();
  const cartContext = useContext(CartContext);
  const { isHiddenCart, setIsHiddenCart } = cartContext as CartContextType;

  // update listOrderUnpaid
  useEffect(() => {
    console.log(order);
    dispatch(updateListOrder(order));
  }, [order.totalQuantity]);

  const handleProfile = async () => {
    navigate("/profile-user/information-user");
  };

  const handleLogout = async () => {
    const res = await logoutService();
    if (res?.status == 401) {
      navigate("/sign-in");
    }
    dispatch(resetUser());
    dispatch(resetOrder());
  };

  const MyAccountPopover = (
    <>
      {user?.username ? (
        <div>
          <div className="menu-item" onClick={handleProfile}>
            <AiOutlineProfile />
            Tài khoản của tôi
          </div>
          <div className="menu-item" onClick={() => navigate("/system/admin")}>
            <AiOutlineGroup />
            Manage System
          </div>
          <div className="menu-item" onClick={handleLogout}>
            <BiLogOut />
            Đăng xuất
          </div>
        </div>
      ) : (
        <div>
          <div className="menu-item" onClick={() => navigate("/sign-in")}>
            Đăng nhập
          </div>
          <div className="menu-item" onClick={() => navigate("/sign-up")}>
            Đăng kí
          </div>
        </div>
      )}
    </>
  );
  const MyCardPopover = <CardHeaderComponent />;

  return (
    <div id="header">
      <div className="container">
        <div className="row">
          <div className="col-md-6">
            <div className="row">
              <div className="col-md-3 header_logo">TTNTK</div>
              <div className="col-md-2 header_item text-center">NAM</div>
              <div className="col-md-2 header_item text-center">NỮ</div>
              <div className="col-md-3 header_item text-center">
                KHUYẾN MẠI
              </div>
            </div>
          </div>
          <div className="col-md-6">
            <div className="row" style={{ height: "75px" }}>
              <div className="col-md-7">
                {isShowSearch && (
                  <div className="header_search">
                    {/* <input
                      type="text"
                      className="header-search_input"
                      placeholder="Nhập để tìm kiếm sản phẩm "
                    />
                    <button className="header-search-btn">
                      <i className="header-search-btn-icon fa-solid fa-magnifying-glass"></i>
                    </button> */}
                    <SearchBarComponent />
                  </div>
                )}
              </div>
              <div className="col-md-5">
                <div className="header-components">
                  <div onClick={() => navigate("/")} className="header-icon">
                    <i className="fa-solid fa-house"></i>
                  </div>
                  <div className="header-account">
                    <Popover
                      id="avatar-popover"
                      placement="bottomRight"
                      content={MyAccountPopover}
                      trigger={"hover"}
                    >
                      <div className="header-icon">
                        <i className="fa-solid fa-user"></i>
                      </div>
                    </Popover>
                  </div>
                  {isShowCart && (
                    <div className="header-cart">
                      <Popover
                        placement="bottomRight" // Vị trí hiển thị popover ("top", "bottom", "left", "right", vv.)
                        content={MyCardPopover}
                        trigger={"hover"}
                        visible={isHiddenCart}
                        onVisibleChange={setIsHiddenCart}
                      >
                        <div
                          className="header-icon"
                          onMouseEnter={() => setIsHiddenCart(true)}
                          onClick={() => navigate("/order")}
                        >
                          <Badge count={order.totalQuantity}>
                            <i className="fa-solid fa-cart-shopping"></i>
                          </Badge>
                        </div>
                      </Popover>
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
