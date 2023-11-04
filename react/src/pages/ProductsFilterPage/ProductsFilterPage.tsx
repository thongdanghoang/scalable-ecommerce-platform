import React, { useEffect, useState } from "react";
import {
  Col,
  Row,
  Spin,
  Badge,
  Pagination,
  Collapse,
  Button,
  Space,
  Cascader,
  Empty,
} from "antd";
import "./ProductsFilter.css";
import CardComponent from "../../components/CardComponent/CardComponent";
import { clothes } from "../../model/ClothesModal";
import {
  getCategories,
  sortAndFilterClothes,
} from "../../services/clothesService";
import { API_URL } from "../../utils/constants";
import { useLocation, useNavigate } from "react-router-dom";
import { convertToSlug } from "../../utils/utils";

interface Option {
  value: string;
  label: string;
  children?: Option[];
}

type QuerySortParams = {
  keyword: string;
  sort: string;
  size: string;
  colour: string;
  category: string;
  page: string;
  limit: "24";
};
type Categories = {
  id: number;
  name: string;
};

const NotFound = (<Empty
  image="https://gw.alipayobjects.com/zos/antfincdn/ZHrcdLPrvN/empty.svg"
  imageStyle={{ height: 300 }}
  description={
    <span>
      Not found
    </span>
  }
>
</Empty>);

// NOTE: get array data from api

async function getProductList(
  filterAndOption: QuerySortParams
): Promise<[clothes[], number]> {
  let params = new URLSearchParams(filterAndOption);
  let data = await sortAndFilterClothes(params);
  let productList: clothes[] = data.products as clothes[];
  productList.forEach(
    (product) =>
      (product.image = `${API_URL}/api/products/images/${product.image}`)
  );
  return [productList, data.totalCount];
}

async function getCategoriesList() {
  let data = (await getCategories()) as Categories[];
  data.forEach((category) => {
    category.name = category.name.toLowerCase();
  });
  return data;
}

function ClothesFilterPage(): React.ReactElement {

  let navigateState = useLocation().state;
  let searchText = navigateState?.searchText;

  const [pagination, setPagination] = useState({
    currentPage: 1,
    totalProduct: 0,
  });
  const handleChangePagination = (value: number) => {
    setPagination((prev) => {
      let current = {
        ...prev,
        currentPage: value,
      };
      return current;
    });
  };

  // spin loading
  const LoadingSpin = (
    <Col className="text-center p-5" span={24}>
      <Spin size="large" />
    </Col>
  );
  let [productRender, setProductRender] = useState([LoadingSpin]);

  // sort options
  const [selectedSortOption, setSelectedOptions] = useState([
    "popular",
    "desc",
  ]);
  const handleChangeSortOption = (value: any[]) => {
    console.log(value);
    setSelectedOptions(() => {
      return value as string[];
    });
  };
  const sortOptions: Option[] = [
    {
      value: "popular",
      label: "phổ biến",
      children: [
        {
          value: "asc",
          label: "tăng dần",
        },
        {
          value: "desc",
          label: "giảm dần",
        },
      ],
    },
    {
      value: "name",
      label: "Tên",
      children: [
        {
          value: "asc",
          label: "tăng dần",
        },
        {
          value: "desc",
          label: "giảm dần",
        },
      ],
    },
    {
      value: "price",
      label: "giá",
      children: [
        {
          value: "asc",
          label: "tăng dần",
        },
        {
          value: "desc",
          label: "giảm dần",
        },
      ],
    },
  ];

  // INFO: this part below contains filter components

  // Size
  const [sizeButtonActivated, setSizeButtonActivated] = useState("");
  const handleClickSizeButton = (size: string) => {
    setSizeButtonActivated((prev) => {
      if (prev == size) {
        return "";
      } else {
        return size;
      }
    });
  };

  // Color
  const [colorButtonActivated, setColorButtonActivated] = useState("");
  const handleClickColorButton = (color: string) => {
    setColorButtonActivated((prev) => {
      if (prev == color) {
        return "";
      } else {
        return color;
      }
    });
  };

  let categoryState = navigateState?.category;
  const [categoryButtonActivated, setCategoryButtonActivated] = useState(categoryState ? categoryState as string : "");
  const handleClickCategoryButton = (category: string) => {
    setCategoryButtonActivated((prev: any) => {
      if (prev == category) {
        return "";
      } else {
        return category;
      }
    });
  };

  let [categoryNameList, setCategoryNameList] = useState([] as Categories[]);
  useEffect(() => {
    const handleCategoriesNameList = async () => {
      let list = await getCategoriesList();
      setCategoryNameList(list);
    };
    handleCategoriesNameList();
  }, []);
  const FilterArea = () => {
    let sizeNameList = [
      "M",
      "L",
      "XL",
      "2XL",
      "3XL",
      "25",
      "26",
      "27",
      "28",
      "29",
      "30",
      "F",
      "S",
    ];
    let colorNameList = [
      "Xanh nhạt",
      "Trắng",
      "Tím than",
      "Xanh đậm",
      "Xanh rêu",
      "Xanh lơ",
      "Đen",
      "Xám",
      "Nâu sữa",
      "Kẻ be",
      "Xanh đá",
      "Be",
      "Rêu",
      "Navy",
      "Ghi",
      "Ghi đậm",
    ];
    // let categoryNameList = [
    //   "NAM",
    //   "NỮ",
    //   "ÁO SƠ MI",
    //   "ÁO THUN",
    //   "ÁO POLO",
    //   "QUẦN ÂU",
    //   "QUẦN SHORT",
    //   "QUẦN ÂU NỮ",
    //   "ĐẦM NỮ - VÁY LIỀN THÂN",
    //   "CHÂN VÁY",
    //   "QUẦN JEAN",
    //   "ÁO KHOÁC",
    //   "ÁO CHỐNG NẮNG",
    // ];

    let sizeButtonComponent = [
      {
        key: "size",
        label: "Kích thước",
        children: (
          <>
            <Space wrap>
              {sizeNameList.map((sizeName) => (
                <Button
                  size="small"
                  type={sizeName == sizeButtonActivated ? "primary" : "dashed"}
                  onClick={() => handleClickSizeButton(sizeName)}
                >
                  {sizeName}
                </Button>
              ))}
            </Space>
          </>
        ),
      },
    ];

    let colorButtonComponent = [
      {
        key: "color",
        label: "Màu sắc",
        children: (
          <>
            <Space wrap>
              {colorNameList.map((color) => (
                <Button
                  size="small"
                  type={color == colorButtonActivated ? "primary" : "dashed"}
                  onClick={() => handleClickColorButton(color)}
                >
                  {color}
                </Button>
              ))}
            </Space>
          </>
        ),
      },
    ];

    let categoryButtonComponent = [
      {
        key: "category",
        label: "Phân loại",
        children: (
          <>
            <Space wrap>
              {categoryNameList.map((category) => (
                <Button
                  size="small"
                  type={
                    category.name == categoryButtonActivated
                      ? "primary"
                      : "dashed"
                  }
                  onClick={() => handleClickCategoryButton(category.name)}
                >
                  {category.name}
                </Button>
              ))}
            </Space>
          </>
        ),
      },
    ];
    // filter components
    return (
      <>
        <Row>
          <Col span={24}>
            <Collapse
              ghost
              className="col-12"
              defaultActiveKey={["size"]}
              items={sizeButtonComponent}
            />
          </Col>
          <Col span={24}>
            <Collapse
              ghost
              defaultActiveKey={["color"]}
              items={colorButtonComponent}
            />
          </Col>
          <Col span={24}>
            <Collapse
              ghost
              defaultActiveKey={["category"]}
              items={categoryButtonComponent}
            />
          </Col>
        </Row>
      </>
    );
  };

  // update page on change
  useEffect(() => {
    setProductRender([LoadingSpin]);
    const productItems = async () => {
      let filterAndOption: QuerySortParams = {
        keyword: searchText ? searchText : "",
        sort: `${selectedSortOption[0]}:${selectedSortOption[1]}`,
        size: sizeButtonActivated,
        colour: colorButtonActivated,
        category: categoryButtonActivated,
        page: (pagination.currentPage - 1).toString(),
        limit: "24",
      };

      let productList = await getProductList(filterAndOption);
      let productItems = productList[0].map((item) => (
        <Col
          xs={24}
          sm={12}
          md={8}
          lg={6}
          // onClick={() => navigate(`/product-detail/`, {state: item.id})}
        >
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
      // handleChangePagination(productList[1]);
      setPagination((prev) => {
        let current = {
          ...prev,
          totalProduct: productList[1],
        };
        return current;
      });
      setProductRender(productItems.length != 0 ? productItems : [NotFound]);
    };
    productItems();
  }, [
    selectedSortOption,
    sizeButtonActivated,
    colorButtonActivated,
    categoryButtonActivated,
    pagination.currentPage,
  ]);

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
                <Cascader
                  allowClear={false}
                  className="selection"
                  defaultValue={["popular", "desc"]}
                  options={sortOptions}
                  onChange={handleChangeSortOption}
                />
              </Col>
            </Row>

            {/* product list */}
            <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }} justify={"center"}>
              {productRender}
            </Row>
          </Col>
        </Row>

        {/* Pagiantion */}
        <div className="d-flex justify-content-center p-5">
          <Pagination
            className="font-weight-normal"
            onChange={handleChangePagination}
            showSizeChanger={false}
            pageSize={10}
            defaultCurrent={1}
            total={pagination.totalProduct}
          />
        </div>
      </div>
    </>
  );
}

export default ClothesFilterPage;
