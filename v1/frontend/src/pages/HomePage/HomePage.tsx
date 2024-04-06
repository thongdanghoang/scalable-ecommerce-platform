import { useEffect, useState } from "react";
import SliderComponent from "../../components/SliderComponent/SliderComponent";
import {
  getAllClothes,
  getClothesOrderBy,
} from "../../services/clothesService";
import "./Home.css";
import { clothes } from "../../model/ClothesModal";
import { Navigate, useNavigate } from "react-router-dom";
import banner from "../../assets/img/banner.png";
import banner1 from "../../assets/img/Thiết kế chưa có tên.png";
import { API_URL } from "../../utils/constants";

async function getProductOrderBy(order: string): Promise<clothes[]> {
  let response = await getClothesOrderBy(order);
  if (response != null) {
    let jsonList = (await response.json()).products as clothes[];
    console.table(jsonList);
    jsonList.forEach((product) => {
      product.image = `${API_URL}/api/products/images/` + product.image;
    });

    return jsonList;
  }
  return [];
}

export default function HomePage() {
  const navigate = useNavigate();
  const [popularProduct, setPopularProduct] = useState([] as clothes[]);
  const [newestProduct, setNewestProduct] = useState([] as clothes[]);
  const [saleProduct, setSaleProduct] = useState([] as clothes[]);

  useEffect(() => {
    const run = async () => {
      setPopularProduct(await getProductOrderBy("popular:desc"));
      setNewestProduct(await getProductOrderBy("newest:asc"));
      setSaleProduct(await getProductOrderBy("price:asc"));
    };
    run();
  }, []);

  const [selectedCategory, setSelectedCategory] = useState("womenCategories");

  return (
    <div className="container" id="homepage">
      <div>
        <SliderComponent
          slidesToShow={1}
          listItems={[banner, banner1]}
          nameSlider={"imagesBackground"}
        />
      </div>
      <div className="gender">
        <div
          className="gender-item"
          onClick={() => setSelectedCategory("menCategories")}
        >
          <div className="gender-item__value">NAM</div>
        </div>
        <div
          className="gender-item"
          onClick={() => setSelectedCategory("womenCategories")}
        >
          <div className="gender-item__value">NỮ</div>
        </div>
      </div>
      <SliderComponent
        slidesToShow={8}
        listItems={[]}
        nameSlider={selectedCategory}
      />
      <div className="header-preview">
        <div className="content">
          <a onClick={() => navigate("/product", {})}>Bán chạy nhất</a>
        </div>
        <div className="more">
          <a onClick={() => navigate("/product", {})}>xem thêm</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_3_image_desktop.jpg?1696059235970"
            alt=""
            onClick={() => navigate("/product", {})}
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={popularProduct}
          nameSlider={"clothesFilter"}
        />
      </div>
      <div className="header-preview">
        <div className="content">
          <a onClick={() => navigate("/product", {state: {sortOption: ["newest", "asc"]}})}>Hàng mới về</a>
        </div>
        <div className="more">
          <a onClick={() => navigate("/product", {state: {sortOption: ["newest", "asc"]}})}>xem thêm</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_2_image_desktop.jpg?1696128283734"
            alt=""
            onClick={() => navigate("/product", {state: {sortOption: ["newest", "asc"]}})}
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={newestProduct}
          nameSlider={"clothesFilter"}
        />
      </div>
      <div className="header-preview">
        <div className="content">
          <a onClick={() => navigate("/product", {state: {sortOption: ["price", "asc"]}})}>Giá sốc</a>
        </div>
        <div className="more">
          <a onClick={() => navigate("/product", {state: {sortOption: ["price", "asc"]}})}>xem thêm</a>
          <i className="fa-solid fa-chevron-right"></i>
        </div>
      </div>
      <div className="product">
        <div className="banner">
          <img
            src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_preivew_sanpham_5_image_desktop.jpg?1696128283734"
            alt=""
            onClick={() => navigate("/product", {state: {sortOption: ["price", "asc"]}})}
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={saleProduct}
          nameSlider={"clothesFilter"}
        />
      </div>
    </div>
  );
}