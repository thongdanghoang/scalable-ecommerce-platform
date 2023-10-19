import { useEffect, useState } from "react";
import SliderComponent from "../../components/SliderComponent/SliderComponent";
import { getAllClothes} from "../../services/clothesService";
import "./Home.css";
import { clothes } from "../../model/ClothesModal";
import { Navigate, useNavigate } from "react-router-dom";

async function getProductsList(): Promise<clothes[]> {
  let response = await getAllClothes();
  if (response != null) {
    let jsonList = (await response.json()).products as clothes[];
    jsonList.forEach((product) => {
        product.image = "http://localhost:8080/api/products/images/"+ product.image;
    });
    console.table(jsonList);
    return jsonList;
  }
  return [];
}

export default function HomePage() {

  const navigate = useNavigate();
  const [productList, setProductList] = useState([] as clothes[]);
  
  useEffect(() => {
    const run = async () => {
      setProductList(await getProductsList());
    }
    run();
  },[])

  return (
    <div className="container" id="homepage">
      <div>
        <SliderComponent
          slidesToShow={1}
          listItems={[
            "https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/slider_2.jpg?1696059235970",
            "https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/slider_3.jpg?1696059235970",
            "https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/slider_4.jpg?1696059235970",
            "https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/slider_5.jpg?1696059235970",
          ]}
          nameSlider={"imagesBackground"}
        />
      </div>
      <div className="gender">
        <div className="gender-item">
          <div className="gender-item__value">
            MEN
          </div>
        </div>
        <div className="gender-item">
          <div className="gender-item__value">
            WOMEN
          </div>
        </div>
      </div>
      <SliderComponent
        slidesToShow={8}
        listItems={[]}
        nameSlider={"typeCagetories"}
      />
      <div className="header-preview">
        <div className="content">
          <a href="">Top Sellers</a>
        </div>
        <div className="more">
          <a href="">More</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_3_image_desktop.jpg?1696059235970"
            alt=""
            onClick={() => navigate('/product' , {})}
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={productList}
          nameSlider={"clothesFilter"}
        />
      </div>
      <div className="header-preview">
        <div className="content">
          <a href="">Recommend</a>
        </div>
        <div className="more">
          <a href="">More</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_2_image_desktop.jpg?1696128283734"
            alt=""
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={productList}
          nameSlider={"clothesFilter"}
        />
      </div>
      <div className="header-preview">
        <div className="content">
          <a href="">Popular</a>
        </div>
        <div className="more">
          <a href="">More</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_5_image_desktop.jpg?1696128283734"
            alt=""
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={productList}
          nameSlider={"clothesFilter"}
        />
      </div>
    </div>
  );
}
