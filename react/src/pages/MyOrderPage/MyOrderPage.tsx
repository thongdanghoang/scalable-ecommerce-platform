import {useNavigate } from "react-router-dom";
import { AiOutlineArrowLeft } from "react-icons/ai";
import './MyOrder.css'

export default function MyOrderPage() {
    const navigate = useNavigate();

    return (<>
        <div className="myOrderorderPage" id="myOrderorderPage">
            <div className="myOrderorderPage-header">
                <div className="content" onClick={() => navigate('/profile-user/order-user')}><AiOutlineArrowLeft/><span>Chi tiết đơn hàng</span></div>
                <div className="date">Ngày tạo: 12:50 24/ 10/ 2023</div>
            </div>
            <div className="myOrderorderPage-body  row">
                <div className='col-md-5'>Trạng thái thanh toán : <span>Chưa thanh toán</span></div>
                <div className='col-md-5'>Trạng thái vận chuyển : <span>Chưa chuyển</span></div>
            </div>
            <div className="myOrderorderPage-body-content row">
                <div className="col-md-5 ">Địa chỉ giao hàng </div>
                <div className="col-md-1"></div>
                <div className="col-md-5 ">Thanh Toán</div>
            </div>
            <div className="myOrderorderPage-body-content row">
                <div className="col-md-5 myOrderorderPage-body-content-address">
                    <div className='name'>Nguyễn Trần Duy Thái</div>
                    <div className='address'>Địa chỉ: <span>abc, Huyện Châu Thành, Long An, aaaaaaaa
                        aaaaaaaaa aaaaaaaaaaaaa aaaaaaaaaaaaaaa</span></div>
                    <div>Số điện thoại: <span>0981642567</span></div>
                </div>
                <div className="col-md-1"></div>
                <div className="col-md-5 myOrderorderPage-body-content-payment">
                    <div>Thanh toán khi nhận hàng (COD)</div>
                </div>
            </div>
            <div className="order-product-header row">
                <div className="col-md-6 order-product-header-title">Sản phẩm</div>
                <div className="col-md-2 order-product-header-title">Đơn giá</div>
                <div className="col-md-2 order-product-header-title">Số lượng</div>
                <div className="col-md-2 order-product-header-title">Tống</div>
            </div>
            <div className="order-product-body row">
                <div className="col-md-6 order-product-body-name">
                    <div className="order-product-body-name-image">
                        <img src="https://bizweb.dktcdn.net/thumb/compact/100/438/408/products/qam3190-nau-2131231.jpg?v=1690163853647" alt=""/>
                    </div>
                    <div className="order-product-body-name-detail">
                        <p>Quần Âu Nam Ống Đứng Vải Nano Cao Cấp Giữ Phom, Co Giãn Thoải Mái</p>
                        <p>Nâu / XL</p>
                        <p>Mã sản phẩm: QAM3190-NAU-XL</p>
                    </div>
                </div>
                <div className="col-md-2 order-product-body-price">
                    <span>439.120đ</span>
                </div>
                <div className="col-md-2 order-product-body-quantity">
                    <span>1</span>
                </div>
                <div className="col-md-2 order-product-body-total">
                    <span>439.120đ</span>
                </div>

            </div>
            <div className="order-product-near-footer row">
                <div className="col-md-8"></div>
                <div className="col-md-2">Phí vận chuyển</div>
                <div className="col-md-2">0đ (Miễn phí vận chuyển đơn hàng từ 200k)</div>
            </div>
            <div className="order-product-footer row">
                <div className="col-md-8"></div>
                <div className="col-md-2">Tổng tiền</div>
                <div className="col-md-2">439.120đ</div>
            </div>
        </div>
            <div className="content" onClick={() => navigate('/profile-user/order-user')}> <button className='back'>QUAY LẠI</button></div>
        </>
    )
}
