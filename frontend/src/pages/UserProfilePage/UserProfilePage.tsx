import { AiOutlineUser } from 'react-icons/ai';
import { constantMenuProfile, toastMSGObject } from '../../utils/utils'
import './UserProfile.css'
import { NavLink, Outlet, useLocation } from 'react-router-dom'
import { BiMap } from 'react-icons/bi';
import {BsLayoutTextWindowReverse} from 'react-icons/bs'
import {CiLock} from 'react-icons/ci'
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { Upload } from 'antd';
import { updateAvatarProfileService, uploadImageProfileService } from '../../services/userService';
import { updateUser } from '../../redux/slides/userSlide';
import { API_URL } from '../../utils/constants';
import {toast} from 'react-toastify'


export default function UserProfilePage() {
    const location = useLocation();
    const user = useSelector((state: RootState) => state.user);
    const dispatch = useDispatch();

    return (
        <div id="UserProfilePage">
            <div className="container">
                <div className="account-page">
                    <div className="grid">
                        <div className="grid__column four-twelfths">
                            <div className="account--sidebar">
                                <div className="account--sidebar-user">
                                    <div className="avatar_icon">
                                        {user.avatar ? (
                                            <img  src={`${API_URL}/api/user/profile/image/${user.avatar}`} />
                                        ) : (
                                            <i className="far fa-user"></i>
                                        )}
                                        <div className="edit-act">
                                            <Upload 
                                                onChange={async (info) => {
                                                    console.log(info.file)
                                                    await uploadImageProfileService(info.file);
                                                    await updateAvatarProfileService(info.file.name)
                                                        .then(res => {
                                                            if(res.success){
                                                                dispatch(updateUser(res.data))
                                                            }
                                                        })
                                                }}
                                                beforeUpload = {(file) => {
                                                    switch (file.type) {
                                                        case 'image/png':
                                                        case 'image/jpg':
                                                        case 'image/jpeg':    
                                                            return false
                                                        default:
                                                            toast.error('Đuôi file ảnh phải là .png , .jpg , .jpeg' , toastMSGObject())
                                                            return Upload.LIST_IGNORE
                                                    }
                                                }}
                                            >
                                                <div className='upload-act'>
                                                    <i className="fas fa-pencil-alt"></i>
                                                    Edit
                                                </div>
                                            </Upload>
                                        </div>
                                    </div>
                                    <div className="fullname">
                                        {user.fullName || user.username}
                                    </div>
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
