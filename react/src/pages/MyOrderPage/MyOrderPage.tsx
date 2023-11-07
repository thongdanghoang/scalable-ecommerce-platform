import {useNavigate, useParams } from "react-router-dom";
import { AiOutlineArrowLeft } from "react-icons/ai";
import './MyOrder.css'
import { cancelOrderService, getDetailOrderService } from "../../services/orderServices";
import { useQuery } from "@tanstack/react-query";
import { API_URL, paymentName } from "../../utils/constants";
import { PaymentMethod } from "../../model/OrderModal";
import { calculatePriceFinal, convertDateAndTime, convertPrice } from "../../utils/utils";
import { clothesCart } from "../../model/ClothesModal";
import { Button, Modal } from "antd";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { addProductToOrder } from "../../redux/slides/orderSlide";

export default function MyOrderPage() {
    const navigate = useNavigate();
    const {code} = useParams();
    const [isOpenModal , setIsOpenModal] = useState(false);
    const dispatch = useDispatch();

    const fetchGetDetailOrder = async (context : any) => {
        const orderId = context?.queryKey[1]
        const res = await getDetailOrderService(orderId);
        return res.data
      }
    
    const {data : orderDetail , isSuccess : isSuccessOrderDetail} = useQuery(['detail-order',code], fetchGetDetailOrder )
    
    const handleCancelOrder = async () => {
        const res = code && await cancelOrderService(code);
        if(res.success){
            setIsOpenModal(true);
        }
    }

    const handleBuyAgain = () => {
        orderDetail?.items?.forEach((item : clothesCart) => {
            dispatch(addProductToOrder(
                {
                    id : item.product.id,
                    amountBuy : item.amount,
                    category : item.product.category,
                    discount : item.product.discount,
                    name : item.product.name,
                    price : item.product.price,
                    classifyClothes : {
                        color : item.classification.colorName,
                        images : `${API_URL}/api/products/images/${item.product.image}`,
                        quantities : {
                            quantityId : item.classification.quantityId,
                            size : item.classification.sizeName,          
                        }
                    }
                }
            ))
        })
        navigate('/order')
    }

    return (
    <>
        <div className="myOrderorderPage" id="myOrderorderPage">
            <div className="myOrderorderPage-header">
                <div className="content" onClick={() => navigate('/profile-user/order-user')}><AiOutlineArrowLeft/><span>Chi tiết đơn hàng</span></div>
                <div className="date">
                    Ngày tạo: {convertDateAndTime(orderDetail?.createdAt).time + " " + convertDateAndTime(orderDetail?.createdAt).date}
                </div>
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
                    <div className='name'>{orderDetail?.address.fullName}</div>
                    <div className='address'>Địa chỉ: <span>
                        {`${orderDetail?.address.addressDetail} , ${orderDetail?.address.ward} , ${orderDetail?.address.district?.split('-')[0]} , ${orderDetail?.address.province?.split('-')[0]}`}
                    </span></div>
                    <div>Số điện thoại: <span>{orderDetail?.address?.phone?.replace('+84', '0')}</span></div>
                </div>
                <div className="col-md-1"></div>
                <div className="col-md-5 myOrderorderPage-body-content-payment">
                    <div>{paymentName(orderDetail?.paymentMethod as PaymentMethod)}</div>
                </div>
            </div>
            <div className="order-product-header row">
                <div className="col-md-6 order-product-header-title">Sản phẩm</div>
                <div className="col-md-2 order-product-header-title">Đơn giá</div>
                <div className="col-md-2 order-product-header-title">Số lượng</div>
                <div className="col-md-2 order-product-header-title">Tống</div>
            </div>
            {orderDetail?.items?.map((item : clothesCart) => (
                <div className="order-product-body row">
                    <div className="col-md-6 order-product-body-name">
                        <div className="order-product-body-name-image">
                            <img src={`${API_URL}/api/products/images/${item.product.image}`} alt=""/>
                        </div>
                        <div className="order-product-body-name-detail">
                            <p>{item.product.name}</p>
                            <p>{item.classification.colorName} / {item.classification.sizeName}</p>
                            <p>Mã sản phẩm: {item.product.id}</p>
                        </div>
                    </div>
                    <div className="col-md-2 order-product-body-price">
                        <span>{convertPrice(calculatePriceFinal(item.product.price , item.product.discount))}</span>
                    </div>
                    <div className="col-md-2 order-product-body-quantity">
                        <span>{item.amount}</span>
                    </div>
                    <div className="col-md-2 order-product-body-total">
                        <span>{convertPrice(calculatePriceFinal(item.product.price , item.product.discount) * item.amount)}</span>
                    </div>
                </div>
            ))}
            <div className="order-product-near-footer row">
                <div className="col-md-8"></div>
                <div className="col-md-2">Phí vận chuyển</div>
                <div className="col-md-2">{convertPrice(orderDetail?.shoppingFee)} (Miễn phí vận chuyển đơn hàng từ 200k)</div>
            </div>
            <div className="order-product-footer row">
                <div className="col-md-8"></div>
                <div className="col-md-2">Tổng tiền</div>
                <div className="col-md-2">{convertPrice(orderDetail?.grandTotal)}</div>
            </div>
        </div>
        <div className="content" onClick={orderDetail?.status === 'CANCELLED' ? handleBuyAgain : handleCancelOrder}> 
            <button className='back'>
                {orderDetail?.status === 'CANCELLED' ? 'Mua lại' : 'Hủy'}
            </button>
        </div>
        
        <Modal 
            className="modal-cancel-order"
            width={'380px'} 
            bodyStyle={{textAlign: "center"}} 
            title="Hủy đơn hàng" 
            open={isOpenModal} 
            footer={null} 
            onCancel={() => setIsOpenModal(false)}
        >
            <img src="https://salt.tikicdn.com/ts/upload/03/b2/49/d6e0011868792350aa44bcbd7e6ffeeb.png" alt="" />
            <p>Đơn hàng của bạn đã được huỷ :
            <br/>
            N3TK mong được tiếp tục phục vụ bạn trong tương lai.
            </p>
            <Button onClick={() => navigate('/')}>Tiếp tục mua sắm</Button>
        </Modal>
    </>
    )
}
