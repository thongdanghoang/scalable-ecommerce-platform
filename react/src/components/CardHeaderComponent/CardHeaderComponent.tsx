import './CardHeader.css'


export default function CardHeaderComponent() {


    return (
        <>
            <div className="header-card-product">
                <div className="header-card-image-product">
                    <a href="">
                        <img
                            src="https://bizweb.dktcdn.net/thumb/compact/100/438/408/products/qam3127-nau-2-1796c057-5e70-467f-aaf7-5290b1b6fbb1.jpg"
                            style={{ width: "83px", height: "124px" }}
                            alt=""
                        />
                    </a>
                </div>
                <div className="header-card-content-product">
                    <div className="header-card-product-info">
                        <div className="header-card-product-info-detail">
                            <a href="">
                                Quần Âu Nam Cao Cấp Giữ Phom, Co Giãn Thoải Mái
                            </a>

                            <div className="card-product-price">
                                <span>998.000đ</span>
                            </div>

                            <div className="card-product-color">
                                <span>Nâu/L</span>
                            </div>
                        </div>
                        <div className="product-delete-button">
                            <i className="fa-solid fa-trash-can"></i>
                        </div>
                    </div>
                    <div className="header-card-product-select">
                        <div className="product-select-btn">
                            <button>-</button>
                            <input
                                value={"1"}
                                placeholder=""
                                type="text"
                                style={{ textAlign: "center" }}
                            />
                            <button>+</button>
                        </div>
                        <div className="product-select-total">
                            <h6>
                                Tổng cộng :<span className="highlight">998.000đ</span>
                            </h6>
                        </div>
                    </div>
                </div>
            </div>
            <div className="addToCard-button">
                <button>Thêm vào giỏ hàng</button>
            </div>
        </>
    )
}
