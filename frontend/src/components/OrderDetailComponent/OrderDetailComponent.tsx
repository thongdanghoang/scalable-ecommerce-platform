import { API_URL, paymentName } from "../../utils/constants";
import "./OrderDetail.css";
import { PaymentMethod } from "../../model/OrderModal";
import {
  calculatePriceFinal,
  convertDateAndTime,
  convertPrice,
} from "../../utils/utils";
import {
  cancelOrderService,
  getDetailOrderService,
} from "../../services/orderServices";
import { useQuery } from "@tanstack/react-query";
import { AiOutlineArrowLeft } from "react-icons/ai";
import { clothesCart } from "../../model/ClothesModal";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { Role } from "../../model/UserModal";
import { addProductToOrder } from "../../redux/slides/orderSlide";

interface propsOrderDetail {
  codeOrder: string;
  handleCancelOrder?: () => void;
}

export default function OrderDetailComponent(props: propsOrderDetail) {
  const { codeOrder, handleCancelOrder } = props;
  const navigate = useNavigate();
  const user = useSelector((state: RootState) => state.user);
  const dispatch = useDispatch();

  const fetchGetDetailOrder = async (context: any) => {
    const orderId = context?.queryKey[1];
    const res = await getDetailOrderService(orderId);
    return res.data;
  };

  const { data: orderDetail, isSuccess: isSuccessOrderDetail } = useQuery(
    ["detail-order", codeOrder],
    fetchGetDetailOrder
  );

  const handleBuyAgain = () => {
    orderDetail?.items?.forEach((item: clothesCart) => {
      dispatch(
        addProductToOrder({
          id: item.product.id,
          amountBuy: item.amount,
          category: item.product.category,
          discount: item.product.discount,
          name: item.product.name,
          price: item.product.price,
          classifyClothes: {
            color: item.classification.colorName,
            images: `${API_URL}/api/products/images/${item.product.image}`,
            quantities: {
              quantityId: item.classification.quantityId,
              size: item.classification.sizeName,
            },
          },
        })
      );
    });
    navigate("/order");
  };

  return (
    <>
      <div className="OrderDetail" id="OrderDetail">
        <div className="OrderDetail-header">
          <div
            className="content"
            onClick={() =>
              user.role === Role["[ROLE_USER]"] &&
              navigate("/profile-user/order-user")
            }
          >
            <AiOutlineArrowLeft />
            <span>Chi tiết đơn hàng</span>
          </div>
          <div className="date">
            Ngày tạo:{" "}
            {convertDateAndTime(orderDetail?.createdAt).time +
              " " +
              convertDateAndTime(orderDetail?.createdAt).date}
          </div>
        </div>
        <div className="OrderDetail-body row">
          <div className="col-md-5">
            Trạng thái đơn hàng : <span>{orderDetail?.status}</span>
          </div>
          <div className="col-md-1"></div>
          <div className="col-md-5">
            Trạng thái thanh toán : <span>{orderDetail?.paymentStatus}</span>
          </div>
        </div>
        <div className="OrderDetail-body-content row">
          <div className="col-md-5 ">Địa chỉ giao hàng </div>
          <div className="col-md-1"></div>
          <div className="col-md-5 ">Thanh Toán</div>
        </div>
        <div className="OrderDetail-body-content row">
          <div className="col-md-5 OrderDetail-body-content-address">
            <div className="name">{orderDetail?.address.fullName}</div>
            <div className="address">
              Địa chỉ:{" "}
              <span>
                {`${orderDetail?.address.addressDetail} , ${
                  orderDetail?.address.ward
                } , ${orderDetail?.address.district?.split("-")[0]} , ${
                  orderDetail?.address.province?.split("-")[0]
                }`}
              </span>
            </div>
            <div>
              Số điện thoại:{" "}
              <span>{orderDetail?.address?.phone?.replace("+84", "0")}</span>
            </div>
          </div>
          <div className="col-md-1"></div>
          <div className="col-md-5 OrderDetail-body-content-payment">
            <div>
              {paymentName(orderDetail?.paymentMethod as PaymentMethod)}
            </div>
          </div>
        </div>
        <div className="order-product-header row">
          <div className="col-md-6 order-product-header-title">Sản phẩm</div>
          <div className="col-md-2 order-product-header-title">Đơn giá</div>
          <div className="col-md-2 order-product-header-title">Số lượng</div>
          <div className="col-md-2 order-product-header-title">Tống</div>
        </div>
        {orderDetail?.items?.map((item: clothesCart) => (
          <div className="order-product-body row">
            <div className="col-md-6 order-product-body-name">
              <div className="order-product-body-name-image">
                <img
                  src={`${API_URL}/api/products/images/${item.product.image}`}
                  alt=""
                />
              </div>
              <div className="order-product-body-name-detail">
                <p>{item.product.name}</p>
                <p>
                  {item.classification.colorName} /{" "}
                  {item.classification.sizeName}
                </p>
                <p>Mã sản phẩm: {item.product.id}</p>
              </div>
            </div>
            <div className="col-md-2 order-product-body-price">
              <span>
                {convertPrice(
                  calculatePriceFinal(item.product.price, item.product.discount)
                )}
              </span>
            </div>
            <div className="col-md-2 order-product-body-quantity">
              <span>{item.amount}</span>
            </div>
            <div className="col-md-2 order-product-body-total">
              <span>
                {convertPrice(
                  calculatePriceFinal(
                    item.product.price,
                    item.product.discount
                  ) * item.amount
                )}
              </span>
            </div>
          </div>
        ))}
        <div className="order-product-near-footer row">
          <div className="col-md-8"></div>
          <div className="col-md-2">Phí vận chuyển</div>
          <div className="col-md-2">
            {convertPrice(orderDetail?.shoppingFee)} (Miễn phí vận chuyển đơn
            hàng từ 200k)
          </div>
        </div>
        <div className="order-product-footer row">
          <div className="col-md-8"></div>
          <div className="col-md-2">Tổng tiền</div>
          <div className="col-md-2">
            {convertPrice(orderDetail?.grandTotal)}
          </div>
        </div>
      </div>
      {user.role === Role["[ROLE_USER]"] && orderDetail?.status === "COMPLETED" && (
        <div
          className="content"
          onClick={
            orderDetail?.status === "CANCELLED"
              ? handleBuyAgain
              : handleCancelOrder
          }
        >
          <button className="back">
            {orderDetail?.status === "CANCELLED" ? "Mua lại" : "Hủy"}
          </button>
        </div>
      )}
    </>
  );
}
