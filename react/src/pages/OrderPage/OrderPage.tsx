import { useDispatch, useSelector } from "react-redux";
import "./Order.css";
import { RootState } from "../../redux/store";
import { calculatePriceFinal, convertPrice, handleChangeAmountBuy } from "../../utils/utils";
import { changeAmount, removeProduct } from "../../redux/slides/orderSlide";
import { clothesOrder } from "../../model/ClothesModal";
import { useNavigate } from "react-router-dom";
import { removeItemOutCartService } from "../../services/cartServices";

export default function OrderPage() {

  const order = useSelector((state:RootState) => state.order); console.log(order);
  const user = useSelector((state: RootState) => state.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSetAmountProduct = (action : string , amountChange : number , orderItem : clothesOrder ) => {
    const amount = handleChangeAmountBuy(action , amountChange , orderItem.classifyClothes.quantities.quantity,);
    if(amount){
      dispatch(changeAmount({
        amountChange : amount,
        orderItem 
      }))
    }
  }

  const handleRemoveProductOutCart = async (proRemove : clothesOrder) => {
    await removeItemOutCartService(proRemove.classifyClothes.quantities.quantityId)
    dispatch(removeProduct(proRemove))
  }

  const handleRedirectPayment = () => {
    if(!user.username){
      navigate('/sign-in' , {state : 'Vui lòng đăng nhập trước khi thanh toán'})
    }else{
      navigate('/order/payment')
    }
  }

  return (
    <div className="container" id="order-page">
      <div className="row">
        <div className="col-md-8">
          <div className="cart-body-item">
            <p className="cart-title">
              <span className="cart-text">GIỎ HÀNG </span>
              <span className="total-cart">
                (<span className="count-item">{order.totalQuantity}</span>) Sản phẩm
              </span>
            </p>
            <div className="cart-header-info">
              <div style={{ minWidth: "52%" }}>Sản phẩm </div>
              <div>Đơn giá</div>
              <div>Số lượng</div>
              <div>Tổng tiền</div>
            </div>
            {order.orderItems.map((item) => (
              <div className="items-available">
                <div className="cart-item">
                  <div className="cart-product">
                    <div className="cart-image">
                      <img
                        src={item.classifyClothes.images[0]}
                        alt=""
                      />
                    </div>
                    <div className="cart-info">
                      <div className="cart-name">
                        <div>
                          {item.name}
                        </div>
                        <span>{`${item.classifyClothes.color}/${item.classifyClothes.quantities.size}`}</span>
                      </div>
                      <div className="cart-item-price">
                        <span className="price">{convertPrice(calculatePriceFinal(item.price , item.discount))}</span>
                      </div>
                      <div className="cart-qty">
                        <div className="cart-select">
                          <div className="cart-select-button">
                            <button 
                              type="button" 
                              className="btn-left"
                              disabled={item.amountBuy<=1}
                              onClick={() => handleSetAmountProduct(
                                  'DECREASE',
                                  item.amountBuy - 1,
                                  item
                              )} 
                            >
                              -
                            </button>
                            <input 
                              type="number"
                              value={item.amountBuy} 
                              onChange={e => handleSetAmountProduct(
                                'INPUT',
                                +e.target.value,
                                item
                              )}
                            />
                            <button 
                              type="button" 
                              className="btn-right"
                              disabled={item.amountBuy>=999}
                              onClick={() => handleSetAmountProduct(
                                  'INCREASE',
                                  item.amountBuy + 1,
                                  item
                              )}
                            >
                              +
                            </button>
                          </div>
                        </div>
                      </div>
                      <div className="cart-total-item-price">
                        <span>{convertPrice(calculatePriceFinal(item.price , item.discount) * item.amountBuy)}</span>
                        <div className="remove-cart" onClick={() => handleRemoveProductOutCart(item)}>
                          <i className="fa-solid fa-trash-can"></i>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
            
          </div>
        </div>
        <div className="col-md-4">
          <div className="total-all">
            <div className="total-bill">
              Tổng đơn: <span>{convertPrice(order.totalPrice)}</span>
            </div>
            <button type="button" onClick={handleRedirectPayment}>Thanh Toán</button>
          </div>
        </div>
      </div>
    </div>
  );
}
