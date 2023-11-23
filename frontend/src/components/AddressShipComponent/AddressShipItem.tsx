import { AiFillDelete, AiOutlineCheckCircle, AiTwotoneEdit } from 'react-icons/ai'
import { AddressShipping } from '../../model/UserModal'
import './Address.css'
import { useLocation } from 'react-router-dom'
import { Button } from 'antd'
import {SetStateAction , Dispatch} from 'react'

interface AddressShipItemProps extends AddressShipping {
    handleShowDetailAddressShip ?: (address : AddressShipping) => void
    handleDeleteAddressShip ?: (idAddressShip : number) => void
    setAddressShipSelect ?: Dispatch<SetStateAction<AddressShipping>>
}

export default function AddressShipItem(props : AddressShipItemProps) {

    const {
        id,
        version,
        fullName,
        phone,
        province,
        district,
        ward,
        addressDetail,
        type,
        default : isDefault,
        handleDeleteAddressShip,
        handleShowDetailAddressShip,
        setAddressShipSelect
    } = props
    const location = useLocation();
    const renderBtn = () => {
        if(location.pathname === '/order/payment'){
            return (
                <Button 
                    type='primary' 
                    className={isDefault ? `btn-address__default` : `btn-address`}
                    onClick={() => setAddressShipSelect && setAddressShipSelect({
                        id,
                        version,
                        fullName,
                        phone,
                        province,
                        district,
                        ward,
                        addressDetail,
                        type,
                        default : isDefault
                    })}
                >
                    Giao đến địa chỉ này
                </Button>
            )
        }else{
            return(
                <>
                    <div className='act-edit' onClick={() => handleShowDetailAddressShip && handleShowDetailAddressShip({
                        id,
                        version,
                        fullName,
                        phone,
                        province,
                        district,
                        ward,
                        addressDetail,
                        type,
                        default : isDefault
                    })}>
                        <AiTwotoneEdit />
                        Chỉnh sửa
                    </div>
                    {!isDefault && (
                        <div className='act-delete' onClick={() => handleDeleteAddressShip && handleDeleteAddressShip(id || 0)}>
                            <AiFillDelete />
                            Xóa
                        </div>
                    )}
                </>
            )
        }
    }

    return (
        <div 
            className="address-ship" 
            style={{ 
                display: location.pathname === '/order/payment' ? 'block' : 'flex' ,
                border: isDefault ? '1px dashed rgb(38, 188, 78)' : '1px solid #c4c4c4'
            }}
        >
            <div className="info">
                <div className="name">
                    {fullName}
                    {isDefault && (
                        <span>
                            <AiOutlineCheckCircle />
                            <span className='ms-2'>Địa chỉ mặc định</span>
                        </span>
                    )}
                </div>
                <div className="address" style={{ marginBottom: 5 }}>
                    <span>Address: </span>
                    {`${addressDetail} , ${ward} , ${district.split('-')[0]} , ${province.split('-')[0]}`}
                </div>
                <div className="phone">
                    <span>Phone: </span>
                    {phone.replace('+84', '0')}
                </div>
            </div>
            <div className="action">
                {renderBtn()}
            </div>
        </div>
    )
}
