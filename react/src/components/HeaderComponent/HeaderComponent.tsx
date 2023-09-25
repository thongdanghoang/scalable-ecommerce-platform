import './header.css'
import logo from '../../assets/img/logo.png'
import {OverlayTrigger, Popover} from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'


export default function HeaderComponent() {
  const navigate = useNavigate()

  const handleProfile = async () => {
    navigate('/profile-user');
  }

  const MyPopover = (
    <Popover id="avatar-popover">
      <Popover.Body>
        <div>
          <div onClick={handleProfile}>User Profile</div>
          <div>Logout</div>
        </div>
      </Popover.Body>
    </Popover>
  );

  return (
    <div id="header">
      <div className="header-with-search">
        <div className="header_logo">
          <img
            className="img1"
            src={logo}
            alt=""
          />
        </div>
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
        <div className="header-cart-account">
          <div className="header-account">
            <OverlayTrigger
              trigger="click" // hoặc "hover" hoặc "focus" tùy theo sự kiện bạn muốn kích hoạt popover
              placement="bottom" // Vị trí hiển thị popover ("top", "bottom", "left", "right", vv.)
              overlay={MyPopover}
            >              
              <i className="header-cart-icon fa-solid fa-user"></i>
            </OverlayTrigger>
          </div>
          <div className="header-cart">
            <i className="header-cart-icon fa-solid fa-cart-shopping"></i>
          </div>
        </div>
      </div>
    </div>
  )
}
