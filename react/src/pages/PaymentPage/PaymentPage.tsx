import React from "react";
import "./Payment.css";
import { useState } from "react";
import type { CollapseProps } from "antd";
import { Collapse } from "antd";

export default function PaymentPage() {
  const quantity = `1 x`;
  const text = `
  Áo thun miền núi chất jdsfk dsjflklf ksdjf;lda `;
  const price = `5000000đ`;

  const CollapseItem: React.FC = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleCollapse = () => {
      setIsOpen(!isOpen);
    };

    const getLabel = () => {
      return isOpen ? "Thu nhỏ" : "Xem chi tiết";
    };

    const items: CollapseProps["items"] = [
      {
        key: "1",
        label: getLabel(),
        children: (
          <div className="d-flex justify-content-around ">
            <div className="payment-product-quantity">{quantity}</div>
            <div className="payment-product-info"> {text}</div>
            <div className="payment-product-price">{price}</div>
          </div>
        ),
      },
    ];

    const onChange = (key: string | string[]) => {
      console.log(key);
      toggleCollapse(); // Toggle the collapse state when the item is clicked
    };

    return (
      <Collapse
        items={items}
        activeKey={isOpen ? ["1"] : undefined}
        onChange={onChange}
      />
    );
  };

  return (
    <div className="container" id="payment-page">
      <div className="row">
        <div className="col-md-9">
          <div className="payment-method">
            <div className="payment-text">Chọn hình thức thanh toán</div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="flexRadioDefault"
              />
              <img src="https://www.coolmate.me/images/momo-icon.png" alt="" />
              <label className="form-check-label">Thanh toán bằng MOMO</label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="flexRadioDefault"
                checked
              />
              <img src="https://www.coolmate.me/images/COD.svg" alt="" />
              <label className="form-check-label">
                Thanh toán khi nhận hàng
              </label>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="payment-address">
            <div className="payment-address-header">
              <div>
                <span>Giao tới</span>
              </div>
              <div className="change-address">Thay đổi</div>
            </div>
            <div className="payment-customer-info">
              <div className="customer-name">Huỳnh Gia Khôi</div>
              <span>|</span>
              <div className="customer-phone">0987654321</div>
            </div>
            <div className="payment-customer-address">
              <span>
                250, khu phố 1, Thị trấn Tầm Vu, Huyện Châu Thành, Long An
              </span>
            </div>
          </div>
          <div className="payment-bill">
            <div className="payment-bill-header">
              <div>Đơn hàng</div>
              <div>1 sản phẩm</div>
            </div>
            <hr />
            <CollapseItem />
            <div className="payment-bill-info">
              <div className="temporary-price">
                <span className="price-text">Tạm tính</span>
                <span>500.000đ</span>
              </div>
              <div className="ship-fee">
                <span className="price-text">Phí vận chuyển</span>
                <span>30.000đ</span>
              </div>
              <div className="ship-promotion">
                <span className="price-text">Khuyến mãi vận chuyển</span>
                <span style={{ color: "rgb(0, 171, 86)" }}>-30.000đ</span>
              </div>
              <hr />
              <div className="payment-total">
                <span>Tổng tiền</span>
                <span
                  style={{
                    color: "rgb(255, 66, 78)",
                    fontSize: "25px",
                    fontWeight: "500",
                  }}
                >
                  500.000đ
                </span>
              </div>
              <button>ĐẶT HÀNG</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
