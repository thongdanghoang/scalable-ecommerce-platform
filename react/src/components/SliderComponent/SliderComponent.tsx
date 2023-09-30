import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

interface PropsSlider {
    slidesToShow : number,
    listItems : Array<any>,
}

export default function SliderComponent(props : PropsSlider) {
    const {
      slidesToShow = 1,
      listItems = []
    } = props

    var settings = {
      dots: true,
      infinite: true,
      speed: 500,
      slidesToShow: 1,
      slidesToScroll: 1
    };
      return (
        <Slider {...settings}>
          {listItems?.map(item => (
            <img src={item} alt="" width={500} height={500}/>
          ))}
        </Slider>
      );
}
