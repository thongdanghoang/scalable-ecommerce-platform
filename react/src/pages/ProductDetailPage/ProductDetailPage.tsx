import { useLocation, useParams } from "react-router-dom";
import SliderComponent from "../../components/SliderComponent/SliderComponent";
import "./ProductDetail.css";
import { useQuery } from "@tanstack/react-query";
import { getClothesById } from "../../services/clothesService";
import { convertPrice, convertToShortNumber } from "../../utils/utils";
import {useState , useEffect, useMemo} from 'react'

export default function ProductDetailPage() {

  const [listSizes , setListSizes] = useState([]);
  const [listImages , setListImages] = useState([]);
  const [activeSize , setActiveSize] = useState(0);
  const { state : id }  = useLocation();

  const fetchGetProductById = async (context : any) => {
    const res = await getClothesById(context?.queryKey[1]);
    return res
  }

  const { data : productDetail , isSuccess} = useQuery(['product-detail',id] , fetchGetProductById)
  console.log(productDetail);

  const handleConvertSizeByColor = (classify : any , index : number) => {
    setActiveSize(index);
    const sizes = classify?.quantities.map((quantity : any) => quantity.size);
    setListSizes(sizes);
    const images = classify?.images.map((img : string) => `http://localhost:8080/api/products/images/${img}`)
    setListImages(images);
  }

  useEffect(() => {
    if(productDetail && isSuccess){
      const firstClassify = productDetail.classifyClothes.shift();
      handleConvertSizeByColor(firstClassify,activeSize);
    }
  },[productDetail,isSuccess])

  const priceRest = useMemo(() => {
    return productDetail?.price - productDetail?.price * productDetail?.discount
  },[productDetail])

  return (
    <div className="container" id="productDetail">
      <div className="row justify-content-between">
        <div className="col-md-4 image-product">
          <SliderComponent
            slidesToShow={1}
            listItems={listImages}
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
                <span>{convertPrice(priceRest)}</span>
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
                {productDetail?.classifyClothes?.map((classify : any , index : number) => (
                  <button
                    className={`product-variation ${activeSize === index ? 'active-size' : ''}`}
                    aria-label="Đen"
                    aria-disabled="false"
                    onClick={() => handleConvertSizeByColor(classify , index)}
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
                {listSizes?.map((size : string) => (
                  <button
                    className="product-variation"
                    aria-label={size}
                    aria-disabled="false"
                  >
                    {size}
                  </button>
                ))}
              </div>
            </section>
          </div>
          <div className="product-quantity">
            <div className="product-select-btn">
              <h5>Số Lượng</h5>
              <button className="product-variation-quantity">-</button>
              <input
                className="product-variation-quantity"
                value={"1"}
                placeholder=""
                type="text"
                style={{ textAlign: "center" }}
              />
              <button className="product-variation-quantity">+</button>
            </div>
          </div>
          <div className="product-submit">
            <button type="button" className="btn btn-primary">
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
