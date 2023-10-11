import React, { useEffect, useState } from "react";
import { Col, Row, Select, Spin, Badge, Pagination } from "antd";
import "./ProductsFilter.css";
import CardComponent from "../../components/CardComponent/CardComponent";
import { clothes } from "../../model/ClothesModal";

// NOTE: test API
async function getProductList(selectPage: string) {
  let param = new URLSearchParams({ page: selectPage });
  console.log(param.toString());
  let response = await fetch(
    `https://picsum.photos/v2/list?${param.toString()}`
  );
  let dataList = await response.json();
  let productList: clothes[] = dataList.map(
    (item: any): clothes => ({
      id: item.id,
      image: item.download_url,
      name: item.author,
      price: item.width,
    })
  );
  return productList;
}

// TODO: implement pagination
function ClothesFilterPage(): React.ReactElement {
  const LoadingSpin = (
    <Col className="text-center p-5" span={24}>
      <Spin size="large" />
    </Col>
  );
  let [productRender, setProductRender] = useState([LoadingSpin]);
  const [select, setSelect] = useState("first");
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
        <Col xs={24} sm={12} md={8} lg={6}>
          <Badge.Ribbon text="new" color="cyan">
            <CardComponent
              id={item.id}
              image={item.image}
              name={item.name}
              price={item.price.toString()}
            />
          </Badge.Ribbon>
        </Col>
      ));
      setProductRender(productItems);
    };
    productItems();
  }, [select]);
  return (
    <>
      <div className="container" id="product-list">
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col md={6} style={{ background: "cyan" }}></Col>

          {/* product selection */}
          <Col md={18}>
            <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
              <Col xs={12} md={16} lg={18}></Col>
              <Col xs={12} md={8} lg={6} className="">
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
            </Row>

            {/* product list */}
            <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
              {productRender}
            </Row>
          </Col>
        </Row>
        <div className="d-flex justify-content-center p-5">
          <Pagination
            className="font-weight-normal"
            onChange={(event) => console.log(event)}
            showSizeChanger={false}
            pageSize={10}
            defaultCurrent={1}
            total={500}
          />
        </div>
      </div>
    </>
  );
}

export default ClothesFilterPage;
