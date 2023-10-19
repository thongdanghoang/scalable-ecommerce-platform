import {useMemo} from "react";
import "./Payment.css";
import { useState } from "react";
import type { CollapseProps } from "antd";
import { Collapse, Modal, Tag } from "antd";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { calculatePriceFinal, convertPrice } from "../../utils/utils";
import { getAddressShipsByUser } from "../../services/userService";
import { useQuery } from "@tanstack/react-query";
import { AddressShipping } from "../../model/UserModal";
import AddressShipItem from "../../components/AddressShipComponent/AddressShipItem";

export default function PaymentPage() {

  const order = useSelector((state:RootState) => state.order); console.log(order);
  const [isOpenModalAddress , setIsOpenModalAddress] = useState(false)

  const fetchGetAddressShipByUser = async () => {
    const res = await getAddressShipsByUser();
    return res.data
  }

  const queryAddressShip = useQuery({queryKey : ['addresses-ship-by-user-1'] , queryFn:fetchGetAddressShipByUser })
  const {data : listAddressShip} = queryAddressShip
  console.log(listAddressShip)

  const addressShipDefault : AddressShipping = useMemo(() => {
    return listAddressShip?.find((as : AddressShipping) => as.default)
  },[listAddressShip])

  const CollapseItem = () => {
    const [isOpen, setIsOpen] = useState(false);

    const items: CollapseProps["items"] = [
      {
        key: "1",
        label: isOpen ? "Thu nhỏ" : "Xem chi tiết",
        children: (
          <>
            {order.orderItems.map(item => (
              <div className="d-flex justify-content-between">
                <div className="payment-product-quantity">{item.amountBuy} x</div>
                <div className="payment-product-info"> {item.name}</div>
                <div className="payment-product-price">{calculatePriceFinal(item.price , item.discount)}</div>
              </div>
            ))}
          </>
        ),
      },
    ];

    const onChangeCollapse = (key: string | string[]) => {
      console.log(key);
      setIsOpen(!isOpen);
    };

    return (
      <Collapse
        items={items}
        activeKey={isOpen ? ["1"] : undefined}
        onChange={onChangeCollapse}
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
              <div className="change-address" onClick={() => setIsOpenModalAddress(true)}>Thay đổi</div>
            </div>           
            <div>
              <div className="payment-customer-info">
                <div className="customer-name">{addressShipDefault?.fullName}</div>
                <span>|</span>
                <div className="customer-phone">{addressShipDefault?.phone?.replace('+84', '0')}</div>
              </div>
              <div className="payment-customer-address">
                <Tag color="green">{addressShipDefault?.type}</Tag>
                {`${addressShipDefault?.addressDetail} , ${addressShipDefault?.ward} , ${addressShipDefault?.district.split('-')[0]} , ${addressShipDefault?.province.split('-')[0]}`}
              </div>
            </div>
          </div>
          <div className="payment-bill">
            <div className="payment-bill-header">
              <div>Đơn hàng</div>
              <div>{`${order.totalQuantity} sản phẩm`}</div>
            </div>
            <hr />
            <CollapseItem />
            <div className="payment-bill-info">
              <div className="temporary-price">
                <span className="price-text">Tạm tính</span>
                <span>{convertPrice(order.totalPrice)}</span>
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
                  {convertPrice(order.totalPrice)}
                </span>
              </div>
              <button>ĐẶT HÀNG</button>
            </div>
          </div>
        </div>
      </div>
      <Modal 
        title="List Address ship" 
        open={isOpenModalAddress} 
        footer={null} 
        onCancel={() => setIsOpenModalAddress(false)}
        width={720}
      >
        <div id="AddressShipComponent">
          <div className="instructor-address">Vui lòng chọn địa chỉ giao hàng có sẵn bên dưới</div>
          {listAddressShip && listAddressShip?.map((address : AddressShipping) => (
            <AddressShipItem 
              key={address.id}
              id={address.id}
              fullName={address.fullName}
              phone={address.phone}
              province={address.province}
              district={address.district}
              ward={address.ward}
              addressDetail={address.addressDetail}
              type={address.type}
              default={address.default}
            />
          ))}
          <div className="redirect-profile">
            Bạn muốn giao hàng đến địa chỉ khác?
            <span> Thêm địa chỉ mới tại đây</span>
          </div>
        </div>
      </Modal>
    </div>
  );
}
