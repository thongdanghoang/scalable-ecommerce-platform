import { Outlet, useNavigate, useParams } from "react-router-dom"
import "./OrderHistory.css"
import { getOrdersByUserService } from "../../services/orderServices";
import { useQuery } from "@tanstack/react-query";
import { convertPrice } from "../../utils/utils";
export default function OrderHistoryComponent() {
  const navigate = useNavigate();
  const {code} = useParams();
  
  const fetchGetOrdersByUser = async () => {
    const res = await getOrdersByUserService();
    return res.data
  }

  const {data : ordersByUser , isSuccess : isSuccessOrdersByUser} = useQuery(['orders-by-user'], fetchGetOrdersByUser )

  return (
    <>
      {code ? (
        <Outlet/>
      ) : (
        <div id='oder-history' >
          <div className="order-history-header">
            <div className="info">Đơn hàng của tôi</div>
            <div className="quantity">1 đơn hàng </div>

          </div>

          <div className="order-history-body">
            <div className="row order-history-body-title">
              <div className="col-md-2 order-history-body-item ">Mã đơn hàng</div>
              <div className="col-md-2 order-history-body-item">Ngày mua</div>
              <div className="col-md-2 order-history-body-item">Địa chỉ</div>
              <div className="col-md-2 order-history-body-item">Giá trị đơn hàng</div>
              <div className="col-md-2 order-history-body-item">Trạng thái thanh toán </div>
              <div className="col-md-2 order-history-body-item">Trạng thái vận chuyển</div>
            </div>
          </div>
          <div className="order-history-detail">
            {ordersByUser ? ordersByUser.map((order : any) => (
              <div className="row">
                <div className="col-md-2 order-history-detail-item"> <span onClick={() => navigate(`/profile-user/order-user/${order.orderId}`)}>{`#0000${order.orderId}`}</span></div>
                <div className="col-md-2 order-history-detail-item">12:50 24/10/2023</div>
                <div className="col-md-2 order-history-detail-item address">
                {`${order.address.addressDetail} , ${order.address.ward} , ${order.address.district?.split('-')[0]} , ${order.address.province?.split('-')[0]}`}
                </div>
                <div className="col-md-2 order-history-detail-item">{convertPrice(order.grandTotal)}</div>
                <div className="col-md-2 order-history-detail-item">{order.paymentStatus}</div>
                <div className="col-md-2 order-history-detail-item">Chưa chuyển</div>
              </div>
            )) : (
              <div>Bạn ko có đơn hàng</div>
            )}
          </div>

        </div>
      )}
    </>

  )
}
