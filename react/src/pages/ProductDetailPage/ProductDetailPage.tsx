import { useLocation, useNavigate} from "react-router-dom";
import SliderComponent from "../../components/SliderComponent/SliderComponent";
import "./ProductDetail.css";
import { useQuery } from "@tanstack/react-query";
import { getClothesById } from "../../services/clothesService";
import { calculatePriceFinal, convertPrice, convertToShortNumber, handleChangeAmountBuy, toastMSGObject } from "../../utils/utils";
import {useState , useEffect, useMemo} from 'react'
import { ToastContainer , toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useDispatch, useSelector} from "react-redux";
import { addProductToOrder } from "../../redux/slides/orderSlide";
import { CartContext, CartContextType } from '../../components/DefaultComponent/DefaultComponent'
import {useContext} from 'react'
import { RootState } from "../../redux/store";

export default function ProductDetailPage() {

  const [activeSize , setActiveSize] = useState<any>({});
  const [activeColor , setActiveColor] = useState<any>({});
  const { state : id }  = useLocation();
  const [amountBuy , setAmountBuy] = useState<number>(1);
  const dispatch = useDispatch();
  const cartContext = useContext(CartContext);
  const {setIsHiddenCart}  = cartContext as CartContextType;
  const user = useSelector((state: RootState) => state.user);
  const navigate = useNavigate();

  const fetchGetProductById = async (context : any) => {
    const res = await getClothesById(context?.queryKey[1]);
    return res
  }

  const { data : productDetail , isSuccess} = useQuery(['product-detail',id] , fetchGetProductById)
  console.log(productDetail);

  const handleGetSizesByColor = (classify : any) => {
    setActiveColor({
      ...classify,
      images : classify?.images.map((img : string) => `http://localhost:8080/api/products/images/${img}`),      
    });
  }

  useEffect(() => {
    if(productDetail && isSuccess){
      const firstClassify = productDetail.classifyClothes[0];
      handleGetSizesByColor(firstClassify);
    }
  },[productDetail,isSuccess])

  const handleSetAmountProduct = (action : string , amountChange : number) => {
    const amount = handleChangeAmountBuy(action , amountChange , activeSize?.quantity as number);
    if(amount){
      setAmountBuy(amount)
    }
  }

  const handleAddProductToOrder = () => {
    if(!user.username){
      navigate('/sign-in' , {state : 'Vui lòng đăng nhập trước khi tạo giỏ hàng'})
    }else if(JSON.stringify(activeSize) === '{}'){
      toast('Please choose size clothes' , toastMSGObject({ theme : 'dark'}));
    }else{
      setIsHiddenCart(true);
      dispatch(addProductToOrder({
        ...productDetail,
        classifyClothes : {
          ...activeColor,
          quantities : {
            ...activeSize
          }
        },
        amountBuy
      }))
      // console.log({
      //   ...productDetail,
      //   classifyClothes : {
      //     ...activeColor,
      //     quantities : {
      //       ...activeSize
      //     }
      //   },
      //   amountBuy
      // })
    }
  }

  return (
    
    <div className="container" id="productDetail">
      <ToastContainer/>
      <div className="row justify-content-between">
        <div className="col-md-4 image-product">
          <SliderComponent
            slidesToShow={1}
            listItems={activeColor?.images}
            nameSlider={"imagesClothes"}
          ></SliderComponent>
        </div>

        <div className="col-md-7">
          <div className="box-divider">
            <h2 className="title-head mb-1">
              {productDetail?.name}
            </h2>
            <div className="product-top">
              <div className="product-sold">
                Đã bán:
                <span className="number-product-sold"> {productDetail?.numberOfSold && convertToShortNumber(productDetail?.numberOfSold)}</span>
              </div>
              <div className="divider"></div>
              <div className="product-review">
                <i className="fa-solid fa-star"></i>
                <i className="fa-solid fa-star"></i>
                <i className="fa-solid fa-star"></i>
                <i className="fa-solid fa-star"></i>
                <i className="fa-solid fa-star"></i>
              </div>
            </div>
            <div className="price">
              <div className="special-price">
                <span>{convertPrice(calculatePriceFinal(productDetail?.price , productDetail?.discount))}</span>
              </div>
              {productDetail?.discount !== 0 && (
                <>
                  <div className="old-price">
                    <span>{convertPrice(productDetail?.price)}</span>
                  </div>
                  <div className="discount-price">
                    <span>{`-${productDetail?.discount}%`}</span>
                  </div>                
                </>
              )}
            </div>
          </div>
          
          <div className="product-color ">
            <section className="flex items-center">
              <div className="color">
                <h5>Màu sắc:</h5>
              </div>

              <div className="flex items-center">
                {productDetail?.classifyClothes?.map((classify : any) => (
                  <button
                    className={`product-variation ${activeColor?.color === classify?.color ? 'active' : ''}`}
                    aria-label="Đen"
                    aria-disabled="false"
                    onClick={() => handleGetSizesByColor(classify)}
                  >
                    {classify?.color}
                  </button>              
                ))}
              </div>
            </section>
          </div>
          <div className="product-size">
            <section className="flex items-center">
              <div className="color">
                <h5>Kích thước:</h5>
              </div>

              <div className="flex items-center">
                {activeColor?.quantities?.map((q : any) => ( // q gồm size và quantity
                  <button
                    className={`product-variation ${activeSize?.size === q.size ? 'active' : ''}`}
                    aria-label={q.size}
                    aria-disabled="false"
                    onClick={() => setActiveSize(q)}
                  >
                    {q.size}
                  </button>
                ))}
              </div>
            </section>
          </div>
          <div className="product-quantity">
            <h5>Số Lượng</h5>
            <div className="product-select-btn">
              <button 
                disabled={amountBuy<=1} 
                onClick={() => handleSetAmountProduct('DECREASE',amountBuy-1) } 
                className="product-variation-quantity"
              >
                -
              </button>
              <input
                className="product-variation-quantity"
                value={amountBuy}
                placeholder=""
                type="number"
                style={{ textAlign: "center" }}
                onChange={(e) => handleSetAmountProduct('INPUT',+e.target.value)}
              />
              <button 
                disabled={amountBuy>=999} 
                onClick={() => handleSetAmountProduct('INCREASE',amountBuy+1)} 
                className="product-variation-quantity"
              >
                +
              </button>
            </div>
          </div>
          <div className="product-submit">
            <button onClick={handleAddProductToOrder} type="button" className="btn btn-primary">
              <i className="fa-solid fa-cart-plus "></i>
              Thêm vào giỏ hàng
            </button>
            <button type="button" className="btn btn-secondary">
              Mua ngay
            </button>
          </div>
          <div className="product-describe">
            <h5>Mô tả sản phẩm</h5>
            <ul>
              <li>Sơ mi nam vải lụa nến lịch lãm, nam tính</li>
              <li>Chất liệu 100% Polyester</li>
              <li>
                Form dáng cơ bản, chỉn chu, có túi ngực, thoải mái khi lên đồ
              </li>
              <li>Vải siêu mướt, thoáng khí, thấm hút mồ hôi hiệu quả</li>
              <li>Áo bắt nhiệt nhanh tạo cảm giác mát mẻ cho người mặc</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
