import "./Footer.css";
export default function FooterComponent() {
  return (
    <div id="FooterComponent">
      <div className="footer">
        <div className="container">
          <div className="row">
            <div className="col-md-3">
              <div className="company">
                <br/>
                <h4>CÔNG TY N3TK</h4>
                <div>
                  Address: R639+HM2, Khu đô thị mới, Thành phố Qui Nhơn, Bình Định 55117, Việt Nam
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="about">
                <h4>VỀ N3TK</h4>
                <div>
                  Chúng tôi tại N3TK cam kết cung cấp sự sáng tạo và chất lượng trong từng sản phẩm, giúp khách hàng thể hiện phong cách cá nhân và tự tin.
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="contact">
                <div className="row">
                  <h4>LIÊN HỆ</h4>
                </div>
                <div className="row mail-icon">
                  <div className="col-md-1">
                    <i className="fa-regular fa-envelope"></i>
                  </div>
                  <div className="col-md-11">
                    Email: tuannhaqe170044@fpt.edu.vn
                  </div>
                </div>
                <div className="row phone">
                  <div className="col-md-1">
                    <i className="fa-solid fa-phone fa-shake"></i>
                  </div>
                  <div className="col-md-11">Phone: 0987654321</div>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="map">
                <iframe
                  src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3874.32986872797!2d109.17271307487015!3d13.819220186579695!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x316f14c59b5e713f%3A0xec90efbee0c9d2b9!2zNTAgTmd1eeG7hW4gSHXhu4csIFRULiBUdXkgUGjGsOG7m2MsIFR1eSBQaMaw4bubYywgQsOsbmggxJDhu4tuaCwgVmlldG5hbQ!5e0!3m2!1sen!2s!4v1699259709205!5m2!1sen!2s"
                  loading="lazy"
                ></iframe>
              </div>
            </div>
          </div>
          <hr style={{ width: "100%", color: "#fff", fontSize: "5px" }} />
        </div>
        <div className="row connect">
          <h4>Kết nối với chúng tôi</h4>
          <div className="icon-connect">
            <i className="fa-brands fa-facebook"></i>
            <i className="fa-brands fa-tiktok"></i>
            <i className="fa-brands fa-instagram"></i>
          </div>
        </div>
      </div>
    </div>
  );
}
