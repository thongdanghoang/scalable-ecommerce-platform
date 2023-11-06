import { AiOutlineCheckCircle } from "react-icons/ai";
import "./PaymentSuccessPage.css";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { OrderCheckout } from "../../model/OrderModal";
import { AddressShipping } from "../../model/UserModal";
import { clothesCart } from "../../model/ClothesModal";
import { convertPrice } from "../../utils/utils";
import { paymentName } from "../../utils/constants";

interface orderSuccess {
  orderCheckout : OrderCheckout,
  addressShipSelect : AddressShipping
}

export default function PaymentSuccessPage() {

  const {state} = useLocation();
  const user = useSelector((state:RootState) => state.user);
  const {orderCheckout , addressShipSelect} = state as orderSuccess
  const navigate = useNavigate();

  console.log(orderCheckout);
  console.log(addressShipSelect)

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
                {`Một email xác nhận đã được gửi tới ${user.email}`}.
                <br />
                Xin vui lòng kiểm tra email của bạn
              </p>
            </div>
          </div>
          <div className="row ">
            <div className="col-md-6 payment-info-customer">
              <h3>Thông tin mua hàng</h3>
              <p>{user.fullName || user.username}</p>
              <p>{user.email}</p>
              <p>{user.phone}</p>
            </div>
            <div className="col-md-6 payment-address-customer">
              <h3>Địa chỉ nhận hàng</h3>
              <p>{addressShipSelect.fullName}</p>
              <p>{addressShipSelect.addressDetail}</p>
              <p>{`${addressShipSelect?.ward} , ${addressShipSelect?.district?.split('-')[0]} , ${addressShipSelect?.province?.split('-')[0]}`}</p>
              <p>{addressShipSelect.phone}</p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-6 payment-method">
              <h3>Phương thức thanh toán</h3>
              <p>{orderCheckout.paymentMethod && paymentName(orderCheckout.paymentMethod)}</p>
            </div>
            <div className="col-md-6 payment-ship-method">
              <h3>Phương thức vận chuyển</h3>
              <p>{orderCheckout.deliveryMethod}</p>
            </div>
          </div>
        </div>
        <div className="col-md-5 payment">
          <h6>Đơn hàng </h6>
          <hr />
          {orderCheckout.items.map((item : clothesCart) => (
            <div className="payment-order-bill-info row">
              <div className="payment-oder-image col-md-2">
                <img
                  src={`http://localhost:8080/api/products/images/${item.product.image}`}
                  alt=""
                  style={{ maxWidth: "100%", maxHeight: "100%" }}
                />
              </div>
              <div className="payment-order-name-color col-md-8">
                <p>{item.product.name}</p>
                <p>{`${item.classification.colorName}/${item.classification.sizeName}`}</p>
              </div>
              <div className="payment-order-price col-md-2">{convertPrice(item.product.price)}</div>
            </div>
          ))}
          <hr />
          <div className="payment-bill-content">
            <div className="payment-bill-title">
              <p>Tạm tính</p>
              <p>Giảm giá</p>
              <p>Phí vận chuyển</p>
              <p>Tổng cộng</p>
            </div>
            <div className="payment-bill-price">
              <p>{convertPrice(orderCheckout.total)}</p>
              <p>{convertPrice(orderCheckout.discount)}</p>
              <p>{convertPrice(orderCheckout.shoppingFee)}</p>
              <p
                style={{
                  fontSize: "20px",
                }}
              >
                {convertPrice(orderCheckout.grandTotal)}
              </p>
            </div>
          </div>
        </div>
        <button
          className="btn btn-warning"
          style={{ maxWidth: "200px", margin: "0 auto", marginTop: "20px" }}
          onClick={() => navigate('/')}
        >
          Tiếp tục mua hàng
        </button>
      </div>
    </div>
  );
}
