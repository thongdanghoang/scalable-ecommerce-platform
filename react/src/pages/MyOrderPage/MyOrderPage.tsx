import { useNavigate, useParams } from "react-router-dom";
import {
  cancelOrderService,
} from "../../services/orderServices";

import { Button, Modal } from "antd";
import { useState } from "react";
import { useDispatch } from "react-redux";
import OrderDetailComponent from "../../components/OrderDetailComponent/OrderDetailComponent";

export default function MyOrderPage() {
  const navigate = useNavigate();
  const { code } = useParams();
  const [isOpenModal, setIsOpenModal] = useState(false);

  const handleCancelOrder = async () => {
    const res = code && (await cancelOrderService(code));
    if (res.success) {
      setIsOpenModal(true);
    }
  };

  return (
    <>
      <OrderDetailComponent codeOrder={code as string} handleCancelOrder={handleCancelOrder} />
      <Modal
        className="modal-cancel-order"
        width={"380px"}
        bodyStyle={{ textAlign: "center" }}
        title="Hủy đơn hàng"
        open={isOpenModal}
        footer={null}
        onCancel={() => setIsOpenModal(false)}
      >
        <img
          src="https://salt.tikicdn.com/ts/upload/03/b2/49/d6e0011868792350aa44bcbd7e6ffeeb.png"
          alt=""
        />
        <p>
          Đơn hàng của bạn đã được huỷ :
          <br />
          N3TK mong được tiếp tục phục vụ bạn trong tương lai.
        </p>
        <Button onClick={() => navigate("/")}>Tiếp tục mua sắm</Button>
      </Modal>
    </>
  );
}
