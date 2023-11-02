import {useNavigate } from "react-router-dom";

export default function MyOrderPage() {
    const navigate = useNavigate();

    return (
        <div>
            <div onClick={() => navigate('/profile-user/order-user')}>Back</div>
        </div>
    )
}
