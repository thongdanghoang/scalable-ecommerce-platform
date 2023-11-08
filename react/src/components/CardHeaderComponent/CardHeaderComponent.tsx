import { useDispatch, useSelector } from 'react-redux';
import './CardHeader.css'
import { RootState } from '../../redux/store';
import { calculatePriceFinal, convertPrice, convertToSlug, handleChangeAmountBuy } from '../../utils/utils';
import { changeAmount, removeProduct } from '../../redux/slides/orderSlide';
import { clothesOrder } from '../../model/ClothesModal';
import OrderEmptyComponent from '../CartEmptyComponent/CartEmptyComponent';
import { useNavigate } from 'react-router-dom';
import { removeItemOutCartService } from '../../services/cartServices';


export default function CardHeaderComponent() {
    const dispatch = useDispatch();
    const order = useSelector((state:RootState) => state.order); console.log(order);
    const navigate = useNavigate();

    const handleRemoveProductOutCart = async (proRemove : clothesOrder) => {
        await removeItemOutCartService(proRemove.classifyClothes.quantities.quantityId)
        dispatch(removeProduct(proRemove))
    }

    const handleSetAmountProduct = (action : string , amountChange : number , orderItem : clothesOrder ) => {
        const amount = handleChangeAmountBuy(action , amountChange , orderItem.classifyClothes.quantities.quantityInStock as number);
        if(amount){
          dispatch(changeAmount({
            amountChange : amount,
            orderItem
          }))
        }
    }

    return (
        <div id="card-popover">
            {order.orderItems.length === 0 ? <OrderEmptyComponent/> : (
                <>
                    <div className="header-card-list">
                        {order.orderItems?.map(item => (
                            <div className="header-card-product" key={item.id}>
                                <div className="header-card-image-product">
                                    <img
                                        src={item?.classifyClothes?.images}
                                        style={{ width: "83px", height: "141px" }}
                                        alt=""
                                    />
                                </div>
                                <div className="header-card-content-product">
                                    <div className="header-card-product-info">
                                        <div className="header-card-product-info-detail">
                                            <div 
                                                className='card-product-name'
                                                onClick={() => navigate(`/product-detail/${convertToSlug(item.name)}` , {state : item.id})}
                                            >
                                                {item.name}                                   
                                            </div>

                                            <div className="card-product-price">
                                                <span>{convertPrice(calculatePriceFinal(item.price , item.discount))}</span>
                                            </div>

                                            <div className="card-product-color">
                                                <span>{`${item.classifyClothes.color}/${item.classifyClothes.quantities.size}`}</span>
                                            </div>
                                        </div>
                                        <div className="product-delete-button">
                                            <div onClick={() => handleRemoveProductOutCart(item)}>
                                                <i className="fa-solid fa-trash-can"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="header-card-product-select">
                                        <div className="product-select-btn">
                                            <button 
                                                disabled={item.amountBuy<=1}
                                                onClick={() => handleSetAmountProduct(
                                                    'DECREASE',
                                                    item.amountBuy - 1,
                                                    item
                                                )} 
                                                className='btn-left'
                                            >
                                                -
                                            </button>
                                            <input
                                                value={item.amountBuy}
                                                placeholder=""
                                                type="number"
                                                style={{ textAlign: "center" }}
                                                onChange={e => handleSetAmountProduct(
                                                    'INPUT',
                                                    +e.target.value,
                                                    item
                                                )}
                                            />
                                            <button
                                                disabled={item.amountBuy>=999}
                                                onClick={() => handleSetAmountProduct(
                                                    'INCREASE',
                                                    item.amountBuy + 1,
                                                    item
                                                )}
                                                className='btn-right'
                                            >
                                                +
                                            </button>
                                        </div>
                                        <div className="product-select-total">
                                            Tổng cộng: <span className="highlight">{convertPrice(calculatePriceFinal(item.price , item.discount) * item.amountBuy)}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="btn-view-order">
                        <div className="total-price-order">
                            <div className="product-select-total">
                                Tổng cộng: <span className="highlight">{convertPrice(order.totalPrice)}</span>
                            </div>
                        </div>
                        <button onClick={() => navigate('/order')}>Xem giỏ hàng</button>
                    </div>
                </>
            )}
        </div>
    )
}
