import './UserProfile.css'
import {useEffect , useState} from 'react'
import { profileService } from "../../services/userService";
import { User } from '../../redux/slides/userSlide';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

export default function UserProfilePage() {
    const user = useSelector((state:RootState)=> state.user); console.log(user);
    const [userProfile , setUserProfile] = useState<User>({...user});
    console.log(userProfile)

    useEffect(() => {
        profileService().then(res => {
            console.log(res)
            // if(res?.success){
            //     setUserProfile(res?.data)
            // }
        })
    },[])

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
                                    <a href="#" className="account--sidebar-item">Account information</a>
                                    <a href="#" className="account--sidebar-item">Address Shipping</a>
                                    <a href="#" className="account--sidebar-item">Order History</a>
                                    <a href="#" className="account--sidebar-item">Linked Bank Acount</a>
                                </div>
                            </div>
                        </div>
                        <div className="grid__column eigth-twelfths">
                            <div id="infotab">
                                <div className="title">Thông tin tài khoản</div>
                                <div className="form-info">
                                    <div className="grid">
                                        <div className="grid__column two-twelfths">
                                            <div className="form-info-user">
                                                <div className="avatar_icon">
                                                    <i className="far fa-user"></i>
                                                    <div className="edit">
                                                        <i className="fas fa-pencil-alt"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="grid__column three-twelfths1">
                                            <div className="grid1">
                                                <div className="grid__column">Username</div>
                                            </div>
                                            <div className="grid">
                                                <div className="grid__column hafl-hafl">Full Name</div>
                                            </div>
                                        </div>
                                        <div className="grid__column seven-twelfths1 flex align--center">
                                            <div className="form-group">
                                                <input
                                                    type="text"
                                                    name="username"
                                                    placeholder="Username"
                                                    className="form-control1"
                                                />
                                                <input
                                                    type="text"
                                                    name="fullname"
                                                    placeholder="Full name"
                                                    className="form-control"
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="grid">
                                    <div className="grid__column three-twelfths flex align--center">Phone Number</div>
                                    <div className="grid__column seven-twelfths ">
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                name="phone"
                                                placeholder="Phone number"
                                                className="form-control"
                                            />
                                        </div>
                                    </div>
                                    <div className="grid__column two-twelfths1">
                                        <div className="form-group">
                                            <button type="submit" className="btn1 form-control2">Update</button>
                                        </div>
                                    </div>
                                </div>
                                <div className="grid">
                                    <div className="grid__column three-twelfths flex align--center">Email</div>
                                    <div className="grid__column seven-twelfths ">
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                name="email"
                                                placeholder="Email"
                                                className="form-control"
                                            />
                                        </div>
                                    </div>
                                    <div className="grid__column two-twelfths1">
                                        <div className="form-group">
                                            <button type="submit" className="btn1 form-control2">Update</button>
                                        </div>
                                    </div>
                                </div>

                                <div className="grid mgb--20">
                                    <div className="grid__column three-twelfths flex align--center">Giới tính</div>
                                    <div className="grid__column seven-twelfths">
                                        <div className="form-group flex" style={{ marginTop: '9px', fontSize: '18px' }}>
                                            <label htmlFor="male" className="custom-radio-label">
                                                <span className="custom-radio">
                                                    <input type="radio" value="male" id="male" />
                                                    <span className="checkmark"></span>
                                                </span>
                                                <span className="label">Nam</span>
                                            </label>
                                            <label htmlFor="female " className="custom-radio-label">
                                                <span className="custom-radio">
                                                    <input type="radio" value="female" id="female" />
                                                    <span className="checkmark"></span>
                                                </span>
                                                <span className="label">Nữ</span>
                                            </label>
                                            <label htmlFor="others" className="custom-radio-label">
                                                <span className="custom-radio">
                                                    <input type="radio" value="others" id="others" />
                                                </span>
                                                <span className="label">Others</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="grid mgb--20">
                                    <div className="grid__column three-twelfths flex align-center">
                                        <div className="bday-label">Ngày sinh
                                            <div className="bday-note">(ngày/tháng/năm)</div>
                                        </div>
                                    </div>
                                    <div className="grid__column seven-twelfths ">
                                        <div className="slider-group">
                                            <div style={{ width: "100%" }}>
                                                <div >
                                                    <div className="grid grid--three-columns">
                                                        <div className="grid__column">
                                                            <div className="datetime-select">
                                                                <div className="form-control">
                                                                    <input className="date" type="date" name="begin"
                                                                        placeholder="dd-mm-yyyy" value=""
                                                                        min="1997-01-01" max="2030-12-31" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="grid">
                                    <div className="grid__column three-twelfths flex align--center">Height</div>
                                    <div className="grid__column seven-twelfths2 ">
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                name="height"
                                                placeholder="cm"
                                                className="form-control"
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="grid">
                                    <div className="grid__column three-twelfths flex align--center">Weight</div>
                                    <div className="grid__column seven-twelfths2 ">
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                name="weight"
                                                placeholder="kg"
                                                className="form-control"
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="grid">
                                    <div className="grid__column flex align--center">
                                        <button className="btn btn-primary btn-block-sm">Cập nhật tài khoản</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
