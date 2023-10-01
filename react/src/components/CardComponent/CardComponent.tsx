import { Card } from "antd";

interface PropsCard {
  image: string;
  name: string;
  price: string;
}

export default function CardComponent(props: PropsCard) {
  const { image, name, price } = props;

  return (
    <Card hoverable style={{ width: 200 }} cover={<img alt="" src={image} />}>
      <div>{name}</div>
      <div>{price}</div>
    </Card>
  );
}
