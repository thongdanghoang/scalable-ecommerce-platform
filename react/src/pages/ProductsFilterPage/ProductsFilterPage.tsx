import React, { useEffect, useState } from "react";
import {
  Col,
  Row,
  Select,
  Spin,
  Badge,
  Pagination,
  Collapse,
  Button,
  Space,
} from "antd";
import "./ProductsFilter.css";
import CardComponent from "../../components/CardComponent/CardComponent";
import { clothes } from "../../model/ClothesModal";

// NOTE: test API
async function getProductList(selectPage: string) {
  let param = new URLSearchParams({ page: selectPage });
  console.log(param.toString());
  let response = await fetch(
    `https://picsum.photos/v2/list?${param.toString()}`,
  );
  let dataList = await response.json();
  let productList: clothes[] = dataList.map(
    (item: any): clothes => ({
      id: item.id,
      image: item.download_url,
      name: item.author,
      price: item.width,
    }),
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

  // INFO: this part below contains filter components
  const [sizeButtonActivated, setSizeButtonActivated] = useState({
    XS: false,
    S: false,
    M: false,
    L: false,
    XL: false,
    XXL: false,
    size28: false,
    size29: false,
    size30: false,
  });
  type ButtonSizeName =
    | "XS"
    | "S"
    | "M"
    | "L"
    | "XL"
    | "XXL"
    | "size28"
    | "size29"
    | "size30";

  const [colorButtonActivated, setColorButtonActivated] = useState({
    red: false,
    orange: false,
    yellow: false,
    green: false,
    cyan: false,
    blue: false,
    purple: false,
  });

  type ButtonColorName =
    | "red"
    | "orange"
    | "yellow"
    | "green"
    | "cyan"
    | "blue"
    | "purple";
  const handleClickSizeButton = (button: ButtonSizeName) => {
    setSizeButtonActivated((prev) => {
      let current = {
        XS: false,
        S: false,
        M: false,
        L: false,
        XL: false,
        XXL: false,
        size28: false,
        size29: false,
        size30: false,
      };
      current[button] = true;
      return current;
    });
  };

  const handleClickColorButton = (button: ButtonColorName) => {
    setColorButtonActivated((prev) => {
      let current = {
        red: false,
        orange: false,
        yellow: false,
        green: false,
        cyan: false,
        blue: false,
        purple: false,
      };
      current[button] = true;
      return current;
    });
  };
  const FilterArea = () => {
    let listSizeButton = [
      {
        key: "size",
        label: "Kích thước",
        children: (
          <>
            <Space wrap>
              <Button
                size="small"
                type={sizeButtonActivated.XS ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("XS")}
              >
                XS
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.S ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("S")}
              >
                S
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.M ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("M")}
              >
                M
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.L ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("L")}
              >
                L
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.XL ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("XL")}
              >
                XL
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.XXL ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("XXL")}
              >
                XXL
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.size28 ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("size28")}
              >
                28
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.size29 ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("size29")}
              >
                29
              </Button>
              <Button
                size="small"
                type={sizeButtonActivated.size30 ? "primary" : "dashed"}
                onClick={() => handleClickSizeButton("size30")}
              >
                30
              </Button>
            </Space>
          </>
        ),
      },
    ];

    let listColorButton = [
      {
        key: "color",
        label: "Màu sắc",
        children: (
          <>
            <Space wrap>
              <Button
                size="small"
                type={colorButtonActivated.red ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("red")}
              >
                Đỏ
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.orange ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("orange")}
              >
                Cam
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.yellow ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("yellow")}
              >
                Vàng
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.green ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("green")}
              >
                Xanh lục
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.cyan ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("cyan")}
              >
                Xanh da trời
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.blue ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("blue")}
              >
                Xanh dương
              </Button>
              <Button
                size="small"
                type={colorButtonActivated.purple ? "primary" : "dashed"}
                onClick={() => handleClickColorButton("purple")}
              >
                Tím
              </Button>
            </Space>
          </>
        ),
      },
    ];
    return (
      <>
        <Row>
          <Col span={24}>
            <Collapse
              ghost
              className="col-12"
              defaultActiveKey={["size"]}
              items={listSizeButton}
            />
          </Col>
          <Col span={24}>
            <Collapse
              ghost
              defaultActiveKey={["color"]}
              items={listColorButton}
            />
          </Col>
        </Row>
      </>
    );
  };

  // NOTE: render page
  return (
    <>
      <div className="container" id="product-list">
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          {/* Filter part*/}
          <Col lg={6}>
            <FilterArea />
          </Col>

          {/* product selection */}
          <Col lg={18}>
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
