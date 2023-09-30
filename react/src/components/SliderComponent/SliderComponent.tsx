import Slider from "react-slick";

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
        slidesToShow,
        slidesToScroll: 1
      };
      return (
        <Slider {...settings}>
          {listItems?.map(item => (
            <div>{item}</div>
          ))}
        </Slider>
      );
}
