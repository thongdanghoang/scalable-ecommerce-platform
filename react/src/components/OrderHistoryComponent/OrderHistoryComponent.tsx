import { Outlet, useNavigate, useParams } from "react-router-dom"

export default function OrderHistoryComponent() {
  const navigate = useNavigate();
  const {code} = useParams();

  return (
    <>
      {code ? (
        <Outlet/>
      ) : (
        <div>
          OrderHistoryComponent
          <div onClick={() => navigate('/profile-user/order-user/123456')}>detail</div>
        </div>
      )}
    </>

  )
}
