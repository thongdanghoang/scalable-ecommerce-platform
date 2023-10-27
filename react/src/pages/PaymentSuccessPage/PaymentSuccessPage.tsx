import React from "react";
import { AiOutlineCheckCircle } from "react-icons/ai";
import "./PaymentSuccessPage.css";
export default function PaymentSuccessPage() {
  return (
    <div className="payment-success container" id="payment-success">
      <div className="row">
        <div className="col-md-7 payment-info">
          <div className="payment-success-thanks row">
            <div className="col-md-2">
              <AiOutlineCheckCircle
                size="90px"
                className="icon-check"
                color="green"
                background-color="white"
              />
            </div>
            <div className="col-md-10">
              <h5>Cảm ơn bạn đã mua hàng</h5>
              <p>
                Một email xác nhận đã được gửi tới thaintdse172872@fpt.edu.vn.
                <br />
                Xin vui lòng kiểm tra email của bạn
              </p>
            </div>
          </div>
          <div className="row ">
            <div className="col-md-6 payment-info-customer">
              <h3>Thông tin mua hàng</h3>
              <p>Nguyễn Trần Duy Thái</p>
              <p>thaintdse172872@fpt.edu.vn</p>
              <p>0987654321</p>
            </div>
            <div className="col-md-6 payment-address-customer">
              <h3>Địa chỉ nhận hàng</h3>
              <p>Nguyễn Trần Duy Thái</p>
              <p>250, khu phố 1</p>
              <p>Thị Trấn Tầm Vu, huyện Châu Thành, Tỉnh Long An</p>
              <p>0987654321</p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-6 payment-method">
              <h3>Phương thức thanh toán</h3>
              <p>Thanh toán khi nhận hàng (COD)</p>
            </div>
            <div className="col-md-6 payment-ship-method">
              <h3>Phương thức vận chuyển</h3>
              <p>Miễn phí vận chuyển đơn hàng từ 500k</p>
            </div>
          </div>
        </div>
        <div className="col-md-5 payment">
          <h6>Đơn hàng </h6>
          <hr />
          <div className="payment-order-bill-info row">
            <div className="payment-oder-image col-md-2">
              <img
                src="https://bizweb.dktcdn.net/thumb/thumb/100/438/408/products/qam3190-nau-2131231.jpg?v=1690163853647"
                alt=""
                style={{ maxWidth: "100%", maxHeight: "100%" }}
              />
            </div>
            <div className="payment-order-name-color col-md-8">
              <p>
                Quần Âu Nam Ống Đứng Vải Nano Cao Cấp Giữ Phom, Co Giãn Thoải
                Mái
              </p>
              <p>Nâu/L</p>
            </div>
            <div className="payment-order-price col-md-2">530.000đ</div>
          </div>
          <hr />
          <div className="payment-bill-content">
            <div className="payment-bill-title">
              <p>Tạm tính</p>
              <p>Phí vận chuyển</p>
              <p>Tổng cộng</p>
            </div>
            <div className="payment-bill-price">
              <p>530.000đ</p>
              <p>Miễn phí</p>
              <p
                style={{
                  fontSize: "20px",
                }}
              >
                530.000đ
              </p>
            </div>
          </div>
        </div>
        <button
          className="btn btn-warning"
          style={{ maxWidth: "200px", margin: "0 auto", marginTop: "20px" }}
        >
          Tiếp tục mua hàng
        </button>
      </div>
    </div>
  );
}
