import { AiOutlineUser } from 'react-icons/ai';
import { constantMenuProfile } from '../../utils/utils'
import './UserProfile.css'
import { NavLink, Outlet, useLocation } from 'react-router-dom'
import { BiMap } from 'react-icons/bi';
import {BsFillCreditCardFill, BsLayoutTextWindowReverse} from 'react-icons/bs'

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
                                    <NavLink to="/profile-user/information-user" className="account--sidebar-item">
                                        <AiOutlineUser/>
                                        Information User
                                    </NavLink>
                                    <NavLink to="/profile-user/address-ship-user" className="account--sidebar-item">
                                        <BiMap/>
                                        Address Shipping
                                    </NavLink>
                                    <NavLink to="/profile-user/order-user" className="account--sidebar-item">
                                        <BsLayoutTextWindowReverse/>
                                        Order History
                                    </NavLink>
                                    <NavLink to="/profile-user/bank-user" className="account--sidebar-item">
                                        <BsFillCreditCardFill/>
                                        Linked Bank Acount
                                    </NavLink>
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
