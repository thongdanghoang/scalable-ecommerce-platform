import "./Order.css";
export default function OrderPage() {
  return (
    <div className="container" id="order-page">
      <div className="row">
        <div className="col-md-8">
          <div className="cart-body-item">
            <p className="cart-title">
              <span className="cart-text">GIỎ HÀNG </span>
              <span className="total-cart">
                (<span className="count-item">2</span>)Sản phẩm
              </span>
            </p>
            <div className="cart-header-info">
              <div style={{ minWidth: "52%" }}>Sản phẩm </div>
              <div>Đơn giá</div>
              <div>Số lượng</div>
              <div>Tổng tiền</div>
            </div>
            <div className="items-available">
              <div className="cart-item">
                <div className="cart-product">
                  <div className="cart-image">
                    <a href="">
                      <img
                        src="https://bizweb.dktcdn.net/thumb/compact/100/438/408/products/qam3127-nau-2-1796c057-5e70-467f-aaf7-5290b1b6fbb1.jpg"
                        alt=""
                      />
                    </a>
                  </div>
                  <div className="cart-info">
                    <div className="cart-name">
                      <a href="">
                        Quần Âu Nam Cao Cấp Giữ Phom, Co Giãn Thoải Mái
                      </a>
                      <div className="spacer"></div>
                      <span>Nâu / L</span>
                    </div>
                    <div className="cart-item-price">
                      <span className="price">499.000đ</span>
                    </div>
                    <div className="cart-qty">
                      <div className="cart-select">
                        <div className="cart-select-button">
                          <button type="button">-</button>
                          <input type="text" />
                          <button type="button">+</button>
                        </div>
                      </div>
                    </div>
                    <div className="cart-total-item-price">
                      <span>499.000đ</span>
                      <div className="spacer"></div>
                      <p className="remove-cart">
                        <i className="fa-solid fa-trash-can"></i>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="total-all">
            <div className="total-bill">
              Tổng đơn: <span>499.000đ</span>
            </div>
            <button type="button">Thanh Toán</button>
          </div>
        </div>
      </div>
    </div>
  );
}
