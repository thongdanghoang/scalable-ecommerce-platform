import React, { useEffect, useState } from "react";
import { Col, Row, Card, Select, Spin } from "antd";
import "./product-page.css";
import CardComponent from "../../components/CardComponent/CardComponent";
interface product {
  id: string;
  image: string;
  name: string;
  price: number;
}

// NOTE: test API
async function getProductList(selectPage: string) {
  let param = new URLSearchParams({ page: selectPage });
  console.log(param.toString());
  let response = await fetch(
    `https://picsum.photos/v2/list?${param.toString()}`
  );
  let dataList = await response.json();
  let productList: product[] = dataList.map(
    (item: any): product => ({
      id: item.id,
      image: item.download_url,
      name: item.author,
      price: item.width,
    })
  );
  return productList;
}

// TODo
function App(): React.ReactElement {
  const LoadingSpin = (
    <Col className="text-center p-5" span={24}>
      <Spin size="large" />
    </Col>
  );

  let [productRender, setProductRender] = useState([LoadingSpin]);
  const [select, setSelect] = useState("1");
  const handleChange = (value: string) => {
    setSelect(() => {
      console.log(value);
      return value;
    });
  };
  useEffect(() => {
    setProductRender([LoadingSpin]);
    const productItems = async () => {
      let productList = await getProductList(select);
      let productItems = productList.map((item) => (
        <Col span={6}>
          <CardComponent
            name={item.name}
            image={item.image}
            price={item.price.toString()}
          />
        </Col>
      ));
      setProductRender(productItems);
    };
    productItems();
  }, [select]);
  return (
    <>
      <div className="container" id="product-list">
        <Col span={24}>
          <Select
            className="selection"
            defaultValue="1"
            onChange={handleChange}
            options={[
              { value: "1", label: "One" },
              { value: "2", label: "Two" },
              { value: "3", label: "Three" },
              { value: "4", label: "Four" },
              { value: "5", label: "Five" },
              { value: "6", label: "Six" },
              { value: "7", label: "Seven" },
              { value: "8", label: "Eight" },
              { value: "9", label: "Nine" },
              { value: "10", label: "Ten" },
            ]}
          />
        </Col>
        <Row>{productRender}</Row>
      </div>
    </>
  );
}

export default App;
