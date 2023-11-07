import { Outlet, useNavigate, useParams } from "react-router-dom"
import "./OrderHistory.css"
import { getOrdersByStatusService, getOrdersByUserService } from "../../services/orderServices";
import { useQuery } from "@tanstack/react-query";
import { convertDateAndTime, convertPrice } from "../../utils/utils";
import { statusOrder } from "../../utils/constants";
import {useState} from 'react'
import OrderEmptyComponent from "../OrderEmptyComponent/OrderEmptyComponent";

export default function OrderHistoryComponent() {
  const navigate = useNavigate();
  const {code} = useParams();
  const [statusOrd , setStatusOrd] = useState('ALL');

  const fetchGetOrdersByStatus = async (context : any) => {
    const statusSelected = context?.queryKey[1];
    let res 
    if(statusSelected === 'ALL'){
      res = await getOrdersByUserService();
    }else{
      res = await getOrdersByStatusService(statusSelected);
    }
    return res.data
  }

  const {data : ordersByStatus , isSuccess : isSuccessOrdersByStatus} = useQuery(['orders-by-status',statusOrd], fetchGetOrdersByStatus )

  return (
    <>
      {code ? (
        <Outlet/>
      ) : (
        <div id='oder-history' >
          <div className="order-history-header">
            <div className="info">Đơn hàng của tôi</div>
            <div className="quantity">{ordersByStatus?.length} đơn hàng </div>
          </div>

          <div className="order-history-tab">
            <div 
              className={`tab-name ${statusOrd === 'ALL' ? 'tab-name-active' : ''}`}
              onClick={() => setStatusOrd('ALL')}
            >
              Tất cả đơn
            </div>
            {statusOrder.map(status => (
              <div 
                className={`tab-name ${status.key === statusOrd && 'tab-name-active'}`}
                onClick={() => setStatusOrd(status.key)}
              >
                {status.value}
              </div>
            ))}
          </div>

          <div className="order-history-body">
            <div className="row order-history-body-title">
              <div className="col-md-2 order-history-body-item ">Mã đơn hàng</div>
              <div className="col-md-2 order-history-body-item">Ngày mua</div>
              <div className="col-md-4 order-history-body-item">Địa chỉ</div>
              <div className="col-md-2 order-history-body-item">Giá trị đơn hàng</div>
              <div className="col-md-2 order-history-body-item">Trạng thái vận chuyển</div>
            </div>
          </div>
          {ordersByStatus?.length !== 0 ? ordersByStatus?.map((order : any) => (
          <div className="order-history-detail">
            <div className="row">
              <div className="col-md-2 order-history-detail-item"> <span onClick={() => navigate(`/profile-user/order-user/${order.orderId}`)}>{`#0000${order.orderId}`}</span></div>
              <div className="col-md-2 order-history-detail-item">
                {convertDateAndTime(order.createdAt).time} <br/>
                {convertDateAndTime(order.createdAt).date}
              </div>
              <div className="col-md-4 order-history-detail-item address">
              {`${order.address.addressDetail} , ${order.address.ward} , ${order.address.district?.split('-')[0]} , ${order.address.province?.split('-')[0]}`}
              </div>
              <div className="col-md-2 order-history-detail-item">{convertPrice(order.grandTotal)}</div>
              <div className="col-md-2 order-history-detail-item">Chưa chuyển</div>
            </div>
          </div>
          )) : (
            <OrderEmptyComponent/>
          )}
        </div>
      )}
    </>

  )
}
