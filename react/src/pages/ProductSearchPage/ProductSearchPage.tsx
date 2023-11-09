import { useLocation } from "react-router-dom";
import { API_URL } from "../../utils/constants";
import { sortAndFilterClothes } from "../../services/clothesService";
import { clothes } from "../../model/ClothesModal";
import { useEffect, useState } from "react";
import { Badge, Cascader, Col, Pagination, Row, Spin } from "antd";
import CardComponent from "../../components/CardComponent/CardComponent";
import './ProductSearchPage.css';
interface Option {
  value: string;
  label: string;
  children?: Option[];
}
type QuerySortParams = {
    keyword: string,
  sort: string;
  page: string;
  limit: "24";
};

async function getProductList(
  filterOptions: QuerySortParams
): Promise<[clothes[], number]> {
  let params = new URLSearchParams(filterOptions);
  let data = await sortAndFilterClothes(params);
  let productList: clothes[] = data.products as clothes[];
  productList.forEach(
    (product) =>
      (product.image = `${API_URL}/api/products/images/${product.image}`)
  );
  return [productList, data.totalCount];
}

export default function ProductSearchPage() {
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
  let searchText = useLocation().state.searchText;
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

  // update page on change
  useEffect(() => {
    setProductRender([LoadingSpin]);
    const productItems = async () => {
      let filterAndOption: QuerySortParams = {
        keyword: searchText,
        sort: `${selectedSortOption[0]}:${selectedSortOption[1]}`,
        page: (pagination.currentPage - 1).toString(),
        limit: "24",
      };

      let productList = await getProductList(filterAndOption);
      let productItems = productList[0].map((item) => (
        <Col xs={24} sm={12} md={8} lg={6}
          // onClick={() => navigate(`/product-detail/`, {state: item.id})}
        >
          <Badge.Ribbon text="new" color="cyan">
            {/* <CardComponent
              id={item.id}
              image={item.image as string}
              name={item.name}
              price={item.price}
              discount={item.discount as number}
            /> */}
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
      setProductRender(productItems);
    };
    productItems();
  }, [
    selectedSortOption,
    pagination.currentPage,
  ]);

    // NOTE: render page
    return (
        <>
          <div className="container" id="product-search-list">
            
    
              {/* product selection */}
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
                <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
                  {productRender}
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
