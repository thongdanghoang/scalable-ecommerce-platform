import './UserProfile.css'
import { Link, Outlet } from 'react-router-dom'

export default function UserProfilePage() {

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
                                    <Link to="/profile-user/information-user" className="account--sidebar-item">Account information</Link>
                                    <Link to="/profile-user/address-ship-user" className="account--sidebar-item">Address Shipping</Link>
                                    <Link to="/profile-user/order-user" className="account--sidebar-item">Order History</Link>
                                    <Link to="/profile-user/bank-user" className="account--sidebar-item">Linked Bank Acount</Link>
                                </div>
                            </div>
                        </div>
                        <div className="grid__column eigth-twelfths">
                            <Outlet/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
