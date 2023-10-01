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
      style={{ width: 200, height: 420 }}
      cover={<img alt="" src={image} />}
    >
      <div className="name">{name}</div>
      <div className="price">{price}</div>
    </Card>
  );
}
