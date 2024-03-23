import "./NotFoundClothes.css";

export default function NotFoundClothesComponent({searchText}: any) {
  return (
    <div id="NotFoundClothesComponen" className="d-flex justify-content-center flex-column">
      <div>
        <span>Tìm kiếm</span>
      </div>
      <div>
        <h5>KẾT QUẢ TÌM KIẾM SẢN PHẨM "{searchText}"</h5>
      </div>

      <div>
        <img
          src="https://bizweb.dktcdn.net/100/438/408/themes/930060/assets/search-page.svg?1699967058610"
          alt=""
        />
      </div>
      <div>
        Tìm kiếm <span>{searchText}</span> của bạn không có sản phẩm phù hợp
      </div>
      <div>HÃY THỬ LẠI CÁCH KHÁC NHƯ</div>
      <div>Sử dụng thuật ngữ chung nhiều hơn</div>
      <div>Kiểm tra chính tả của bạn</div>
    </div>
  );
}
