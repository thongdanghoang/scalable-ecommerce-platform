import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
interface PropsSlider {
  slidesToShow: number,
  listItems: Array<any>,
  nameSlider: String
}

export default function SliderComponent(props: PropsSlider) {
  const { slidesToShow = 1, listItems = [] , nameSlider } = props;

    var settings = {
      dots: true,
      infinite: true,
      speed: 500,
      slidesToShow,
      slidesToScroll: 1
    };

    const renderSlider = () => {
      switch (nameSlider) {
        case 'imagesBackground':
          return (
            <Slider {...settings}>
              {listItems?.map(item => (
                <img src={item} alt="" width={500} height={500}/>
              ))}
            </Slider>
          )
        case '':
          return (
            <Slider {...settings}>
              {listItems?.map(item => (
                <img src={item} alt="" width={500} height={500}/>
              ))}
            </Slider>
          )
        default:
          return <></>
      }
    }

    return (
      renderSlider()
    );
}
