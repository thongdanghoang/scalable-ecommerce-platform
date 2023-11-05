import "./Payment.css";
import { useState , useMemo , useEffect } from "react";
import type { CollapseProps } from "antd";
import { Collapse, Modal, Tag } from "antd";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { calculatePriceFinal, convertPrice } from "../../utils/utils";
import { getAddressShipsByUser } from "../../services/userService";
import { useQuery } from "@tanstack/react-query";
import { AddressShipping } from "../../model/UserModal";
import AddressShipItem from "../../components/AddressShipComponent/AddressShipItem";
import LoadingComponent from "../../components/LoadingComponent/LoadingComponent";
import { checkoutInfoService, checkoutService } from "../../services/checkoutServices";
import { useNavigate } from "react-router-dom";
import { OrderCheckout } from "../../model/OrderModal";
import { paymentImage } from "../../utils/constants";

export default function PaymentPage() {

  const order = useSelector((state:RootState) => state.order); console.log(order);
  const [isOpenModalAddress , setIsOpenModalAddress] = useState(false);
  const [paymentMethod , setPaymentMethod] = useState('CASH_ON_DELIVERY');
  const [addressShipSelect , setAddressShipSelect] = useState<AddressShipping>({} as AddressShipping);
  const navigate = useNavigate();

  // handle Address ship api
  const fetchGetAddressShipByUser = async () => {
    const res = await getAddressShipsByUser();
    return res.data
  }

  const queryAddressShip = useQuery({queryKey : ['addresses-ship-by-user-1'] , queryFn:fetchGetAddressShipByUser })
  const {data : listAddressShip , isSuccess : isSuccessListAddressShip} = queryAddressShip
  queryAddressShip.refetch();
  console.log(listAddressShip)

  // const addressShipDefault : AddressShipping = useMemo(() => {
  //   console.log(listAddressShip)
  //   return listAddressShip?.find((as : AddressShipping) => as.default)
  // },[isSuccessListAddressShip,listAddressShip])

  useEffect(() => {
    isSuccessListAddressShip && setAddressShipSelect(listAddressShip?.find((as : AddressShipping) => as.default))
  },[isSuccessListAddressShip, listAddressShip])

  useEffect(() => {
    isSuccessListAddressShip && setIsOpenModalAddress(false)
  },[addressShipSelect])

  //handle checkout info api

  const fetchCheckoutInfo = async () => {
    const res : OrderCheckout = await checkoutInfoService();
    return res
  }

  const queryCheckoutInfo = useQuery({queryKey : ['checkout-info'] , queryFn:fetchCheckoutInfo })
  const {data : orderCheckout , isSuccess : isSuccessOrderCheckout} = queryCheckoutInfo

  console.log(orderCheckout)

  //handle checkout order api

  const handleCheckoutOrder = () => {
    console.log({
      addressId : addressShipSelect.id,
      paymentMethod,
      deliveryMethodDto : "STANDARD_DELIVERY"
    })
    // checkoutService({
    //   addressId : addressShipSelect.id,
    //   paymentMethod,
    //   deliveryMethodDto : "STANDARD_DELIVERY"
    // })
    //   .then(res => {
    //     console.log(res);
    //     navigate('/payment/success' , 
    //       { state : {
    //         orderCheckout : {
    //           ...orderCheckout,
    //           paymentMethod,
    //           deliveryMethod : orderCheckout?.availableDeliveryMethods && orderCheckout?.availableDeliveryMethods[0]
    //         } , 
    //         addressShipSelect
    //       } }
    //     )
    //   })

    navigate('/payment/success' , 
      { state : {
        orderCheckout : {
          ...orderCheckout,
          paymentMethod,
          deliveryMethod : orderCheckout?.availableDeliveryMethods && orderCheckout?.availableDeliveryMethods[0]
        } , 
        addressShipSelect
      } }
    )
  }

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
            {orderCheckout?.availablePaymentMethods && 
            orderCheckout?.availablePaymentMethods.map((availablePayM) => (
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="radio"
                  name="flexRadioDefault"
                  onChange={e => setPaymentMethod(e.target.value)}
                  value={availablePayM}
                  checked = {paymentMethod === availablePayM}
                />
                <img src={paymentImage(availablePayM)} alt="" />
                <label className="form-check-label">{availablePayM}</label>
              </div>
            ))}
          </div>
          <div className="payment-method">
            <div className="payment-text">Chọn hình thức vận chuyển</div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="sss"
                checked
              />
              <img src="https://cdn-icons-png.flaticon.com/512/6213/6213198.png" alt="" />
              <label className="form-check-label">Vận chuyển tiêu chuẩn </label>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <LoadingComponent isloading={!isSuccessListAddressShip}>
            <div className="payment-address">
              <div className="payment-address-header">
                <div>
                  <span>Giao tới</span>
                </div>
                <div className="change-address" onClick={() => setIsOpenModalAddress(true)}>Thay đổi</div>
              </div>           
              <div>
                <div className="payment-customer-info">
                  <div className="customer-name">{addressShipSelect?.fullName}</div>
                  <span>|</span>
                  <div className="customer-phone">{addressShipSelect?.phone?.replace('+84', '0')}</div>
                </div>
                <div className="payment-customer-address">
                  <Tag color="green">{addressShipSelect?.type}</Tag>
                  {`${addressShipSelect?.addressDetail} , ${addressShipSelect?.ward} , ${addressShipSelect?.district?.split('-')[0]} , ${addressShipSelect?.province?.split('-')[0]}`}
                </div>
              </div>
            </div>
          </LoadingComponent>
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
                <span>{convertPrice(orderCheckout?.total)}</span>
              </div>
              <div className="ship-promotion">
                <span className="price-text">Giảm giá</span>
                <span style={{ color: "rgb(0, 171, 86)" }}>-{convertPrice(orderCheckout?.discount)}</span>
              </div>
              <div className="ship-fee">
                <span className="price-text">Phí vận chuyển</span>
                <span>{convertPrice(orderCheckout?.shoppingFee)}</span>
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
                  {convertPrice(orderCheckout?.grandTotal)}
                </span>
              </div>
              <button onClick={handleCheckoutOrder}>ĐẶT HÀNG</button>
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
              setAddressShipSelect={setAddressShipSelect}
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
