import SliderComponent from "../../components/SliderComponent/SliderComponent";
import "./ProductDetail.css";

export default function ProductDetailPage() {
  return (
    <>
      <div className="container">
        <div className="row image-product">
          <div className="col-md-5">
            <SliderComponent
              slidesToShow={1}
              listItems={[
                "https://bizweb.dktcdn.net/100/438/408/products/spm5005-den-ao-so-mi-nam-yody-1.jpg?v=1688723412400",
                "https://bizweb.dktcdn.net/100/438/408/products/spm5005-tra-7.jpg?v=1690163742317",
              ]}
              nameSlider={"image"}
            ></SliderComponent>
          </div>

          <div className="col-md-7">
            <div className="box-divider">
              <h2 className="title-head mb-1">
                Áo Sơ Mi Nam Tay Dài Thoáng Khí Siêu Mát
              </h2>
              <div className="product-top">
                <div className="product-sold">
                  Đã bán:
                  <span className="number-product-sold">32.7K</span>
                </div>
                <div className="divider"></div>
                <div className="product-review">
                  <i className="fa-solid fa-star"></i>
                  <i className="fa-solid fa-star"></i>
                  <i className="fa-solid fa-star"></i>
                  <i className="fa-solid fa-star"></i>
                  <i className="fa-solid fa-star"></i>
                </div>
              </div>
              <div className="price">
                <div className="special-price">
                  <span>290.000đ</span>
                </div>
                <div className="old-price">
                  <span>300.000đ</span>
                </div>
                <div className="discount-price">
                  <span>-10%</span>
                </div>
              </div>
            </div>
            <div className="product-color ">
              <section className="flex items-center">
                <div className="color">
                  <h5>Màu sắc:</h5>
                </div>

                <div className="flex items-center">
                  <button
                    className="product-variation"
                    aria-label="Đen"
                    aria-disabled="false"
                  >
                    Đen
                  </button>
                  <button
                    className="product-variation"
                    aria-label="Đen"
                    aria-disabled="false"
                  >
                    Xám
                  </button>
                  <button
                    className="product-variation"
                    aria-label="Đen"
                    aria-disabled="false"
                  >
                    Trắng
                  </button>
                  <button
                    className="product-variation"
                    aria-label="Đen"
                    aria-disabled="false"
                  >
                    Đỏ
                  </button>
                </div>
              </section>
            </div>
            <div className="product-size">
              <section className="flex items-center">
                <div className="color">
                  <h5>Kích thước:</h5>
                </div>

                <div className="flex items-center">
                  <button
                    className="product-variation"
                    aria-label="M"
                    aria-disabled="false"
                  >
                    M
                  </button>
                  <button
                    className="product-variation"
                    aria-label="L"
                    aria-disabled="false"
                  >
                    L
                  </button>
                  <button
                    className="product-variation"
                    aria-label="XL"
                    aria-disabled="false"
                  >
                    XL
                  </button>
                  <button
                    className="product-variation"
                    aria-label="2Xl"
                    aria-disabled="false"
                  >
                    2XL
                  </button>
                </div>
              </section>
            </div>
            <div className="product-quantity">
              <h5>Số Lượng:</h5>
              <button
                className="product-variation"
                aria-label="M"
                aria-disabled="false"
              >
                M
              </button>
            </div>
            <div className="product-submit">
              <button type="button" className="btn btn-primary">
                <i className="fa-solid fa-cart-plus "></i>
                Thêm vào giỏ hàng
              </button>
              <button type="button" className="btn btn-primary">
                Mua ngay
              </button>
            </div>
            <div className="product-describe">
              <h5>Mô tả sản phẩm</h5>
              <ul>
                <li>Sơ mi nam vải lụa nến lịch lãm, nam tính</li>
                <li>Chất liệu 100% Polyester</li>
                <li>
                  Form dáng cơ bản, chỉn chu, có túi ngực, thoải mái khi lên đồ
                </li>
                <li>Vải siêu mướt, thoáng khí, thấm hút mồ hôi hiệu quả</li>
                <li>Áo bắt nhiệt nhanh tạo cảm giác mát mẻ cho người mặc</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
