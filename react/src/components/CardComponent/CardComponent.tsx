import { Card } from "antd";
import "./Card.css";
import { useNavigate } from "react-router-dom";
import { convertToSlug } from "../../utils/utils";
interface PropsCard {
  id : number
  image: string;
  name: string;
  price: string;
}

export default function CardComponent(props: PropsCard) {
  const {id, image, name, price } = props;
  const navigate = useNavigate();

  return (
    <Card
      id="card"
      hoverable
      cover={<img height={'100%'} alt="" src={image} />}
      onClick={() => navigate(`product-detail/${convertToSlug(name)}` , {state : id})}
    >
      <div className="name">{name}</div>
      <div className="price">{price}</div>
    </Card>
  );
}
