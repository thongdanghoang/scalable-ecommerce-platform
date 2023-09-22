import './header.css'
import logo from '../../assets/img/logo.png'

export default function HeaderComponent() {
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
            <i className="header-cart-icon fa-solid fa-user"></i>
          </div>
          <div className="header-cart">
            <i className="header-cart-icon fa-solid fa-cart-shopping"></i>
          </div>
        </div>
      </div>
    </div>
  )
}
