import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import CardComponent from "../CardComponent/CardComponent";
import "../SliderComponent/Slider.css";
import leftArrow from "../../assets/icons/left-arrow.svg";
import rightArrow from "../../assets/icons/right-arrow.svg";
import { getCategories } from "../../services/clothesService";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import fakeData from "../../data/categories.json";

interface PropsSlider {
  slidesToShow: number;
  listItems: Array<any>;
  nameSlider: String;
}

export default function SliderComponent(props: PropsSlider) {
  const navigate = useNavigate();
  const { slidesToShow = 1, listItems = [], nameSlider } = props;
  let [typeCategory, setTypeCategory] = useState([] as any[]);

  useEffect(() => {
    const handleTypeCategory = async () => {
      setTypeCategory(await getCategories());
    };
    handleTypeCategory();
  }, []);

  const arrowCustome = () => {
    switch (nameSlider) {
      case "imagesBackground":
      case "imagesClothes":
        return {
          prevArrow: (
            <img
              alt=""
              src="https://icones.pro/wp-content/uploads/2021/06/symbole-fleche-gauche-gris.png"
            />
          ),
          nextArrow: (
            <img
              alt=""
              src="https://icones.pro/wp-content/uploads/2021/06/symbole-fleche-droite-grise.png"
            />
          ),
        };
      case "clothesFilter":
        return {
          prevArrow: <img src={leftArrow} />,
          nextArrow: <img src={rightArrow} />,
        };
      default:
        return {
          prevArrow: <img src={leftArrow} />,
          nextArrow: <img src={rightArrow} />,
        };
    }
  };

  var settings = {
    dots: false,
    infinite: true,
    slidesToShow,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    ...arrowCustome(),
  };
  var settings_nameSlider = {
    dots: false,
    infinite: true,
    slidesToShow,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 5000,
    ...arrowCustome(),
  };
  var settings_typeCategories_clothesFilter = {
    dots: false,
    infinite: true,
    slidesToShow,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 5000,
    ...arrowCustome(),
  };

  const renderSlider = () => {
    switch (nameSlider) {
      case "imagesBackground":
        return (
          <Slider {...settings_nameSlider} className="slider-img-bg">
            {listItems?.map((item) => (
              <img src={item} alt="" width="100%" height={500} />
            ))}
          </Slider>
        );
      case "clothesFilter":
        return (
          <Slider
            {...settings_typeCategories_clothesFilter}
            className="slider-arrow-custome"
          >
            {listItems?.map((item) => (
              <div className="custome-card-home-page" style={{ width: 250 }}>
                <CardComponent
                  id={item.id}
                  name={item.name}
                  price={item.price}
                  image={item.image}
                  discount={item.discount}
                />
              </div>
            ))}
          </Slider>
        );
      // case "typeCategories":
      //   return (
      //     <div className="slider-cate">
      //       <Slider
      //         className="slider-arrow-custome"
      //         {...settings_typeCategories_clothesFilter}
      //       >
      //         {typeCategory.map((item) => (
      //           <div className="typeCagetories">
      //             <img
      //               src="https://bizweb.dktcdn.net/100/438/408/themes/919724/assets/home_danhmuc_2_child_2_image.png?1696308204036"
      //               alt=""
      //             />
      //             <div className="title">{item.name}</div>
      //           </div>
      //         ))}
      //       </Slider>
      //     </div>
      //   );
      case "menCategories":
        return (
          <div className="slider-cate">
            <Slider className="slider-arrow-custome" {...settings}>
              {fakeData
                .filter((item) => item.name.includes("NAM"))
                .map((item) => (
                  <div
                    className="typeCagetories"
                    onClick={() =>
                      navigate("/product", {
                        state: { category: item.name.toLowerCase() },
                      })
                    }
                  >
                    <img src={item.image} style={{ maxWidth: "80%" }} alt="" />
                    <div className="title">{item.name}</div>
                  </div>
                ))}
            </Slider>
          </div>
        );
      case "womenCategories":
        return (
          <div className="slider-cate">
            <Slider className="slider-arrow-custome" {...settings}>
              {fakeData
                .filter((item) => item.name.includes("NỮ"))
                .map((item) => (
                  <div
                    className="typeCagetories"
                    onClick={() =>
                      navigate("/product", {
                        state: { category: item.name.toLowerCase() },
                      })
                    }
                  >
                    <img src={item.image} style={{ maxWidth: "80%" }} alt="" />
                    <div className="title">{item.name}</div>
                  </div>
                ))}
            </Slider>
          </div>
        );
      case "imagesClothes":
        return (
          <Slider className="slider-img-bg" {...settings}>
            {listItems?.map((item) => (
              <div className="image">
                <img src={item} alt="" />
              </div>
            ))}
          </Slider>
        );
      default:
        return <></>;
    }
  };

  return renderSlider();
}
