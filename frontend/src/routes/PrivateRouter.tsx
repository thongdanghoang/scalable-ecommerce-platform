import { useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { Navigate, useLocation } from "react-router-dom";
import { Role } from "../model/UserModal";

export default function PrivateRouter({children , role} : {children : React.ReactNode , role: Role[]}) {

    const user = useSelector((state: RootState) => state.user);
    const location = useLocation();

    if(user.username){ // đã đăng nhập , start phân quyền truy cập page
        if(role.length === 0 || !role.includes(user.role as Role)){ 
            // những trang này đã đăng nhập rồi thì ko truy cập được
            // hoặc những trang ko truy cập được do ko thỏa role
            return <Navigate to={'/'} /> 
        }else{ 
            return children
        }
    }else{ // chưa đăng nhập
        if(location.pathname === '/order/payment' ||
           location.pathname === '/payment/success' ||
           location.pathname === '/system'){ // đây là những trang phải login mới truy cập được
            return <Navigate to={'/'} />
        }
        return children 
    }
}
