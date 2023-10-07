import SliderComponent from "../../components/SliderComponent/SliderComponent";
import "./HomePage.css";

export default function HomePage() {
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
        <div className="men">
          <a href="">MEN</a>
          <div className="underline"></div>
        </div>
        <div className="women">
          <a href="">WOMEN</a>
          <div className="underline"></div>
        </div>
      </div>
      <SliderComponent
        slidesToShow={8}
        listItems={[]}
        nameSlider={"menu"}
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
          />
        </div>
        <SliderComponent
          slidesToShow={4}
          listItems={[{}]}
          nameSlider={"card"}
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
          listItems={[{}]}
          nameSlider={"card"}
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
          listItems={[{}]}
          nameSlider={"card"}
        />
      </div>
    </div>
  );
}
