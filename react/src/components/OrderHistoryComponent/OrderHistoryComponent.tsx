import { Outlet, useNavigate, useParams } from "react-router-dom"
import "./OrderHistory.css"
export default function OrderHistoryComponent() {
  const navigate = useNavigate();
  const {code} = useParams();

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
            <div className="row">
              <div className="col-md-2 order-history-detail-item"> <span onClick={() => navigate('/profile-user/order-user/123456')}>#00001</span></div>
              <div className="col-md-2 order-history-detail-item">12:50 24/10/2023</div>
              <div className="col-md-2 order-history-detail-item address">abc, Huyện Châu Thành, Long An, Vietnam</div>
              <div className="col-md-2 order-history-detail-item">439.120đ</div>
              <div className="col-md-2 order-history-detail-item">Chưa thu tiền</div>
              <div className="col-md-2 order-history-detail-item">Chưa chuyển</div>
            </div>
          </div>

        </div>
      )}
    </>

  )
}
