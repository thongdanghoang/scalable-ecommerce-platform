import { constantMenuProfile } from '../../utils/utils'
import './UserProfile.css'
import { NavLink, Outlet, useLocation } from 'react-router-dom'

export default function UserProfilePage() {
    const location = useLocation();

    return (
        <div id="UserProfilePage">
            <div className="container">
                <div className="account-page">
                    <div className="grid">
                        <div className="grid__column four-twelfths">
                            <div className="account--sidebar">
                                <div className="title flex align--center justify--between">
                                    Nguyễn Trần Duy Thái
                                </div>
                                <div className="account--sidebar-items">
                                    <NavLink to="/profile-user/information-user" className="account--sidebar-item">Information User</NavLink>
                                    <NavLink to="/profile-user/address-ship-user" className="account--sidebar-item">Address Shipping</NavLink>
                                    <NavLink to="/profile-user/order-user" className="account--sidebar-item">Order History</NavLink>
                                    <NavLink to="/profile-user/bank-user" className="account--sidebar-item">Linked Bank Acount</NavLink>
                                </div>
                            </div>
                        </div>
                        <div className="grid__column eigth-twelfths">
                            <div id="infotab">
                                <div className="title">{constantMenuProfile(location.pathname)}</div>
                                <Outlet/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
