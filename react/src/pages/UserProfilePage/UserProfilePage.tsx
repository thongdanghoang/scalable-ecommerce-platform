import { AiOutlineUser } from 'react-icons/ai';
import { constantMenuProfile } from '../../utils/utils'
import './UserProfile.css'
import { NavLink, Outlet, useLocation } from 'react-router-dom'
import { BiMap } from 'react-icons/bi';
import {BsLayoutTextWindowReverse} from 'react-icons/bs'
import {CiLock} from 'react-icons/ci'
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';


export default function UserProfilePage() {
    const location = useLocation();
    const user = useSelector((state: RootState) => state.user);
    console.log(user)

    return (
        <div id="UserProfilePage">
            <div className="container">
                <div className="account-page">
                    <div className="grid">
                        <div className="grid__column four-twelfths">
                            <div className="account--sidebar">
                                <div className="grid__column two-twelfths-avatar">
                                    <div className="form-info-user-avatar">
                                        <div className="avatar_icon">
                                            <i className="far fa-user"></i>
                                            <div className="edit">
                                                <i className="fas fa-pencil-alt"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="title-infouser-name flex align--center justify--between">
                                    {user.username}
                                </div>
                                <div className="account--sidebar-items">
                                    <NavLink to="/profile-user/information-user" className="account--sidebar-item ">
                                        <AiOutlineUser/>
                                        Tài khoản của tôi
                                    </NavLink>
                                    <NavLink to="/profile-user/address-ship-user" className="account--sidebar-item">
                                        <BiMap/>
                                        Sổ địa chỉ
                                    </NavLink>
                                    <NavLink to="/profile-user/order-user" className="account--sidebar-item">
                                        <BsLayoutTextWindowReverse/>
                                        Đơn hàng của tôi
                                    </NavLink>
                                    <NavLink to="/profile-user/change-password" className="account--sidebar-item">
                                        <CiLock/>
                                        Đổi mật khẩu
                                    </NavLink>
                                </div>
                            </div>
                        </div>
                        <div className="grid__column eigth-twelfths">
                            <div id="infotab">
                                <div className="title-infouser">{constantMenuProfile(location.pathname)}</div>
                                <Outlet/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
