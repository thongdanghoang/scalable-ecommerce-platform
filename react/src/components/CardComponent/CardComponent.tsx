import { Card } from "antd";
import "./Card.css";
interface PropsCard {
  image: string;
  name: string;
  price: string;
}

export default function CardComponent(props: PropsCard) {
  const { image, name, price } = props;

  return (
    <Card
      id="card"
      hoverable
      cover={<img height={'100%'} alt="" src={image} />}
    >
      <div className="name">{name}</div>
      <div className="price">{price}</div>
    </Card>
  );
}
