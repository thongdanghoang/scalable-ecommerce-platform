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
  Alert,
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
import NotFoundClothesComponent from "../../components/NotFoundClothesComponent/NotFoundClothesComponent";

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
  limit: "12";
};
type Categories = {
  id: number;
  name: string;
};

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
  });
  const [totalProduct, setTotalProduct] = useState(0);
  const handleChangePagination = (value: number) => {
    setPagination((prev) => {
      let current = {
        // ...prev,
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
  let sortOptionState = navigateState?.sortOption;
  const [selectedSortOption, setSelectedOptions] = useState(
    sortOptionState ? (sortOptionState as string[]) : ["popular", "desc"]
  );
  const handleChangeSortOption = (value: any[]) => {
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
    {
      value: "newest",
      label: "thời gian",
      children: [
        {
          value: "asc",
          label: "mới nhất",
        },
        {
          value: "desc",

          label: "cũ nhất",
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
  const [categoryButtonActivated, setCategoryButtonActivated] = useState(
    categoryState ? (categoryState as string) : ""
  );
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
                <button
                  // size="small"
                  // type={sizeName == sizeButtonActivated ? "primary" : "default"}
                  onClick={() => handleClickSizeButton(sizeName)}
                  className={`btn btn-sm ${
                    sizeName == sizeButtonActivated ? "btn-dark" : "btn-light"
                  }`}
                >
                  {sizeName}
                </button>
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
                <button
                  className={`btn btn-sm ${
                    color == colorButtonActivated ? "btn-dark" : "btn-light"
                  }`}
                  onClick={() => handleClickColorButton(color)}
                >
                  {color}
                </button>
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
                <button
                  className={`btn btn-sm ${
                    category.name == categoryButtonActivated
                      ? "btn-dark"
                      : "btn-light"
                  }`}
                  onClick={() => handleClickCategoryButton(category.name)}
                >
                  {category.name}
                </button>
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
    setPagination((prev) => ({ ...prev, currentPage: 1 }));
  }, [
    selectedSortOption,
    sizeButtonActivated,
    colorButtonActivated,
    categoryButtonActivated,
  ]);

  useEffect(() => {
    const handleSearchAndFilter = async () => {
      setProductRender([LoadingSpin]);
      let filterAndOption: QuerySortParams = {
        keyword: searchText ? searchText : "",
        sort: `${selectedSortOption[0]}:${selectedSortOption[1]}`,
        size: sizeButtonActivated,
        colour: colorButtonActivated,
        category: categoryButtonActivated,
        page: (pagination.currentPage - 1).toString(),
        limit: "12",
      };
      let NotFound;
      if (
        filterAndOption.size.length == 0 &&
        filterAndOption.colour.length == 0 &&
        filterAndOption.category.length == 0
      ) {
        NotFound = (
          <NotFoundClothesComponent
            searchText={filterAndOption.keyword}
          ></NotFoundClothesComponent>
        );
      } else {
        NotFound = (
          <Col span={24}>
            <Alert
              message="Không có sản phẩm nào trong danh mục này"
              type="warning"
            />
          </Col>
        );
      }

      let productList = await getProductList(filterAndOption);
      let productItems = productList[0].map((item) => (
        <Col xs={24} sm={12} md={8} lg={6}>
          <CardComponent
            id={item.id}
            image={item.image as string}
            name={item.name}
            price={item.price}
            discount={item.discount as number}
            rated={item.rated as number}
            numberOfSold={item.numberOfSold as number}
          />
        </Col>
      ));
      // handleChangePagination(productList[1]);
      setTotalProduct(productList[1]);
      setProductRender(productItems.length != 0 ? productItems : [NotFound]);
    };
    handleSearchAndFilter();
  }, [pagination]);

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
                  defaultValue={selectedSortOption}
                  options={sortOptions}
                  onChange={handleChangeSortOption}
                />
              </Col>
            </Row>

            {/* product list */}
            <Row
              gutter={[
                { xs: 8, sm: 16, md: 24, lg: 32 },
                { xs: 8, sm: 16, md: 24, lg: 32 },
              ]}
              className="mt-2"
            >
              {productRender}
            </Row>
          </Col>
        </Row>

        {/* Pagiantion */}
        <div className="d-flex justify-content-center p-5">
          <Pagination
            onChange={handleChangePagination}
            showSizeChanger={false}
            pageSize={12}
            defaultCurrent={1}
            current={pagination.currentPage}
            total={totalProduct}
          />
        </div>
      </div>
    </>
  );
}

export default ClothesFilterPage;
