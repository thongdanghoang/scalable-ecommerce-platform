import { useLocation, useNavigate} from "react-router-dom";
import SliderComponent from "../../components/SliderComponent/SliderComponent";
import "./ProductDetail.css";
import { useQuery } from "@tanstack/react-query";
import { getClothesById } from "../../services/clothesService";
import { calculatePriceFinal, convertPrice, convertToShortNumber, handleChangeAmountBuy, toastMSGObject } from "../../utils/utils";
import {useState , useEffect} from 'react'
import { toast } from 'react-toastify';
import { useDispatch, useSelector} from "react-redux";
import { addProductToOrder } from "../../redux/slides/orderSlide";
import { CartContext, CartContextType } from '../../components/DefaultComponent/DefaultComponent'
import {useContext} from 'react'
import { RootState } from "../../redux/store";
import { createCartService } from "../../services/cartServices";
import { API_URL } from "../../utils/constants";
import parse from 'html-react-parser';

export default function ProductDetailPage() {

  const [activeSize , setActiveSize] = useState<any>({}); console.log(activeSize)
  const [activeColor , setActiveColor] = useState<any>({}); console.log(activeColor)
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

  const { data : productDetail , isSuccess} = useQuery(['product-detail-0',id] , fetchGetProductById)
  console.log(productDetail);

  const handleGetSizesByColor = (classify : any) => {
    setActiveColor({
      ...classify,
      images : classify?.images.map((img : string) => `${API_URL}/api/products/images/${img}`),      
    });
  }

  useEffect(() => {
    if(productDetail && isSuccess){
      const firstClassify = productDetail.classifyClothes[0];
      handleGetSizesByColor(firstClassify);
    }
  },[productDetail,isSuccess])

  const handleSetAmountProduct = (action : string , amountChange : number) => {
    if(JSON.stringify(activeSize) !== '{}'){
      const amount = handleChangeAmountBuy(action , amountChange , activeSize.quantityInStock );
      if(amount){
        setAmountBuy(amount)
      }
    }else{
      toast('☹️ Vui lòng chọn size sản phẩm' , toastMSGObject({ theme : 'dark'}));
    }
  }

  const handleAddProductToOrder = async (isRedirectPayment : boolean) => {
    if(!user.username){
      navigate('/sign-in' , {state : 'Vui lòng đăng nhập trước khi tạo giỏ hàng'})
    }else if(JSON.stringify(activeSize) === '{}'){
      toast('☹️ Vui lòng chọn size sản phẩm' , toastMSGObject({ theme : 'dark'}));
    }else{
      setIsHiddenCart(true);
      await createCartService({
        quantityId : activeSize.quantityId,
        amount : amountBuy
      })
      dispatch(addProductToOrder({
          ...productDetail,
          classifyClothes : {
            ...activeColor,
            images : activeColor.images[0],
            quantities : {
              ...activeSize
            }
          },
          amountBuy
      }))
      isRedirectPayment && navigate('/order')
    }
    //   {
    //     product : {
    //       id : productDetail.id,
    //       sku : productDetail.sku,
    //       name : productDetail.name,
    //       image : productDetail.image,
    //       price : productDetail.price,
    //       discount : productDetail.discount,
    //       numberOfSold : productDetail.numberOfSold,
    //       rated : productDetail.rate,
    //       category : productDetail.category
    //     },
    //     classification : {
    //       quantityId : productDetail.classifyClothes.quantities.quantityId,
    //       sizeName : productDetail.classifyClothes.quantities.sizeName,
    //       colorName : productDetail.classifyClothes.color
    //     },
    //     amount : amountBuy
    //   }
    // }
  }

  const handleBuyNow = async () => {
    await handleAddProductToOrder(true);
  }

  return (
    
    <div className="container" id="productDetail">
      <div className="row justify-content-between align-items-start">
        <div className="col-md-4 images-product">
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
                    <span>{`-${productDetail?.discount*100}%`}</span>
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
                    className={`product-variation ${activeSize?.size === q.size ? 'active' : ''} ${q.quantityInStock === 0 ? 'soldout' : ''}`}
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
          {user.role === '[ROLE_USER]' && (
            <>
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
                <button 
                  onClick={() => handleAddProductToOrder(false)} 
                  type="button" 
                  className="btn btn-primary"
                >
                  <i className="fa-solid fa-cart-plus "></i>
                  Thêm vào giỏ hàng
                </button>
                <button 
                  onClick={handleBuyNow} 
                  type="button" 
                  className="btn btn-secondary"
                >
                  Mua ngay
                </button>
              </div>           
            </>
          )}
          <div className="product-describe">
            {productDetail?.description && parse(productDetail.description)}
          </div>
        </div>
      </div>
    </div>
  );
}
