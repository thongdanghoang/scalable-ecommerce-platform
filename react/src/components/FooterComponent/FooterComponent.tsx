import "./Footer.css";
export default function FooterComponent() {
  return (
    <div id="FooterComponent">
      <div className="footer">
        <div className="container">
          <div className="row">
            <div className="col-md-3">
              <div className="company">
                <h4>TTNTK COMPANY</h4>
                <div>
                  Address: 50 macarona, blabla town, blabla city, blabla
                  province
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="about">
                <h4>About Us</h4>
                <div>
                  About Our TTNTK Our Style Coupons Accessibility Statement
                  Shipping Policy Security Policy
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="contact">
                <div className="row">
                  <h4>Contact Us</h4>
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
                  src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3874.584811996225!2d109.21427447704302!3d13.803884352025454!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x316f6bf778c80973%3A0x8a7d0b5aa0af29c7!2zxJDhuqFpIGjhu41jIEZQVCBRdXkgTmjGoW4!5e0!3m2!1svi!2s!4v1695972730591!5m2!1svi!2s"
                  loading="lazy"
                ></iframe>
              </div>
            </div>
          </div>
          <hr style={{ width: "100%", color: "#fff", fontSize: "5px" }} />
        </div>
        <div className="row connect">
          <h4>Conncet with us</h4>
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
