import '../../pages/UserProfilePage/UserProfile.css'
import {useEffect , useState} from 'react'
import { profileService, updateProfileService, verifyEmailService } from "../../services/userService";
import { updateUser } from '../../redux/slides/userSlide';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { User } from '../../model/UserModal';
import { ToastContainer , toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import OTPInput, {} from 'react-otp-input'
import { Button, Modal } from 'react-bootstrap';
import './InforUser.css'

export default function InforUserComponent() {
    const user = useSelector((state:RootState)=> state.user);
    const [userProfile , setUserProfile] = useState<User>({...user});
    const dispatch = useDispatch();
    console.log(userProfile);
    const [otp, setOtp] = useState('');
    const [isOpenModal , setIsOpenModal] = useState(false);
    //const spanEle = useRef<HTMLElement | null>(null)

    useEffect(() => {
        profileService().then(res => {
            console.log(res)
            if(res?.success){
                setUserProfile(res?.data)
            }
        })
    },[user])

    const handleUpdateProfile = async () => {
        const res = await updateProfileService({
            version : userProfile.version,
            username : userProfile.username,
            fullName : userProfile.fullName,
            gender : userProfile.gender.toUpperCase(),
            birthday : userProfile.birthday.split('-').reverse().join('/'),
            weight : userProfile.weight,
            height : userProfile.height
        })
        console.log(res)
        if(res?.success){
            dispatch(updateUser(res?.data));
            toast.success(<span style={{color: "#07bc0c"}}>{res?.message}</span>, {
                position: "top-right",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
                });
        }else{
            toast.error(<span style={{color: "red"}}>{res?.message}</span>, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
                });
        }
    }

    const handleOnChangeProfile = (e : React.ChangeEvent<HTMLInputElement>) => {
        setUserProfile({
            ...userProfile,
            [e.target.name] : e.target.value
        })
    }

    const handleSendOTPEmail = async () => {
        setIsOpenModal(true)
        const res = await verifyEmailService(userProfile?.email,'');
        console.log(res)
    }

    const handleCloseOTPForm = () => {
        setIsOpenModal(false);
        setOtp('')
    }

    const handleVerifyEmail = async () => {
        const res = await verifyEmailService(userProfile?.email , otp);
        if(res?.success){
            toast.success(<span style={{color: "#07bc0c"}}>{res?.message}</span>, {
                position: "top-right",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
            dispatch(updateUser({...user, emailVerified : true}));
            handleCloseOTPForm();
        }else{
            toast.error(<span style={{color: "red"}}>{res?.message}</span>, {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        }
    }

    return (
        <>
            <div className="form-info">
                <ToastContainer />
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
                                value={userProfile.username}
                                placeholder="Username"
                                className="form-control1"
                                onChange={handleOnChangeProfile}
                            />
                            <input
                                type="text"
                                name="fullName"
                                value={userProfile.fullName}
                                placeholder="Full name"
                                className="form-control"
                                onChange={handleOnChangeProfile}
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
                            value={userProfile.phone}
                            placeholder="Phone number"
                            className="form-control"
                            onChange={handleOnChangeProfile}
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
                            disabled={userProfile.emailVerified}
                            type="text"
                            name="email"
                            value={userProfile.email}
                            placeholder="email"
                            className="form-control"
                            onChange={handleOnChangeProfile}
                        />
                    </div>
                </div>
                <div className="grid__column two-twelfths1">
                    {userProfile.emailVerified ? (
                        <div className='has-verified'>
                            <p>Đã xác thực</p>
                        </div>
                    ) : (
                        <div className="form-group">
                            <button onClick={handleSendOTPEmail} type="submit" className="btn1 form-control2">Update</button>
                        </div>
                    )}
                </div>
            </div>

            <div className="grid mgb--20">
                <div className="grid__column three-twelfths flex align--center">Giới tính</div>
                <div className="grid__column seven-twelfths">
                    <div className="form-group flex" style={{ marginTop: '9px', fontSize: '18px' }}>
                        <label htmlFor="male" className="custom-radio-label">
                            <span className="custom-radio">
                                <input
                                    type="radio"
                                    name="gender"
                                    value="male"
                                    id="male"
                                    checked={userProfile.gender?.toLowerCase() === 'male'}
                                    onChange={handleOnChangeProfile}
                                />
                                <span className="checkmark"></span>
                            </span>
                            <span className="label">Nam</span>
                        </label>
                        <label htmlFor="female " className="custom-radio-label">
                            <span className="custom-radio">
                                <input
                                    type="radio"
                                    name="gender"
                                    value="female"
                                    id="female"
                                    checked={userProfile.gender?.toLowerCase() === 'female'}
                                    onChange={handleOnChangeProfile}
                                />
                                <span className="checkmark"></span>
                            </span>
                            <span className="label">Nữ</span>
                        </label>
                        <label htmlFor="others" className="custom-radio-label">
                            <span className="custom-radio">
                                <input
                                    type="radio"
                                    name="gender"
                                    value="other"
                                    id="other"
                                    checked={userProfile.gender?.toLowerCase() === 'other'}
                                    onChange={handleOnChangeProfile}
                                />
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
                                                <input className="date" type="date" name="birthday"
                                                    placeholder="dd-mm-yyyy" value={userProfile.birthday?.split('/').reverse().join('-')}
                                                    onChange={handleOnChangeProfile}
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
                            type="number"
                            name="height"
                            value={userProfile.height}
                            placeholder="cm"
                            className="form-control"
                            onChange={handleOnChangeProfile}
                        />
                    </div>
                </div>
            </div>
            <div className="grid">
                <div className="grid__column three-twelfths flex align--center">Weight</div>
                <div className="grid__column seven-twelfths2 ">
                    <div className="form-group">
                        <input
                            type="number"
                            name="weight"
                            value={userProfile.weight}
                            placeholder="kg"
                            className="form-control"
                            onChange={handleOnChangeProfile}
                        />
                    </div>
                </div>
            </div>
            <div className="grid">
                <div className="grid__column flex align--center">
                    <button onClick={handleUpdateProfile} className="btn btn-primary btn-block-sm">Cập nhật tài khoản</button>
                </div>
            </div>
            <Modal
                show={isOpenModal}
                onHide={handleCloseOTPForm}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>Enter OTP Email</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{`To verify the email is yours, enter the 6-digit verification code just sent to ${userProfile.email}`}</p>
                    <OTPInput
                        containerStyle={{ justifyContent: "center" }}
                        value={otp}
                        onChange={setOtp}
                        numInputs={6}
                        renderSeparator={<span>-</span>}
                        renderInput={(props) => <input {...props} />}
                        inputType='number'
                        inputStyle={{
                            width: '3rem',
                            height: '3rem',
                            margin: '0 5px',
                            fontSize: '2rem',
                            borderRadius: '4px',
                            border: '1px solid rgba(0,0,0,.3)'
                        }}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button style={{ width: "100%" }} variant="danger" onClick={handleVerifyEmail}>
                        Confirm
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
