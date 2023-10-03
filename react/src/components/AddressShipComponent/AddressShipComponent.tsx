import {AiOutlinePlus , AiOutlineCheckCircle , AiTwotoneEdit} from 'react-icons/ai'
import './Address.css'
import { Button, Input, Modal, Select , Form, Checkbox } from 'antd'
import { useEffect, useState } from 'react'
import { getListDistricts, getListProvincesCity, getListWards } from '../../utils/utils';

// const addressShipping = [
//   {
//     city :
//   }
// ]

export default function AddressShipComponent() {
  const [isModalOpen , setIsOpenModal] = useState(false);
  const [listProvinces , setListProvinces] = useState([]);
  const [listDistricts , setListDistricts] = useState([]); 
  const [listWards , setListWards] = useState([]); 
  const [addressShipping , setAddressShipping] = useState({
    fullName : '',
    city : '',
    district : '',
    ward : '',
    address : '',
  })

  console.log(addressShipping)

  const handleCancelModal = () => {
    setIsOpenModal(false);
  }

  useEffect(() => {
    const fetchProvincesCity = async () => {
      const data = await getListProvincesCity()
      setListProvinces(data)
    } 
    fetchProvincesCity();
  },[])

  const handleOnChangeProvince = async (nameCity : string, value : any) => {
    setAddressShipping({
      ...addressShipping ,
      city : nameCity,
      district : '',
      ward : ''
    })
    if(nameCity){
      setListDistricts([]);
      setListWards([])
    }
    setListDistricts(await getListDistricts(value.key))
  }

  const handleOnChangeDistrict = async (nameDistrict : string, value : any) => {
    setAddressShipping({
      ...addressShipping ,
      district : nameDistrict
    })
    if(nameDistrict){
      setListWards([])
    }
    setListWards(await getListWards(value.key))
  }

  return (
    <div id='AddressShipComponent'>
      <div className="add-address" onClick={() => setIsOpenModal(true)}>
        <AiOutlinePlus />
        <span>Add new address</span>
      </div>
      <div className="address-ship">
        <div className="info">
          <div className="name">
            Kim Tan Lê
            <span>
              <AiOutlineCheckCircle />
              <span className='ms-2'>Default address</span>
            </span>
          </div>
          <div className="address" style={{marginBottom:5}}>
            <span>Address: </span>
            52 Lê Lợi
          </div>
          <div className="phone">
            <span>Phone: </span>
            0935187859
          </div>
        </div>
        <div className="action">
          <AiTwotoneEdit/>
          Edit
        </div>
      </div>
      <Modal title="Thêm địa chỉ mới" open={isModalOpen} footer={null} onCancel={handleCancelModal}>
        <Form
          name="wrap"
          labelCol={{ flex: '130px' }}
          labelAlign="left"
          labelWrap
          wrapperCol={{ flex: 1 }}
          colon={false}
          style={{ maxWidth: 600 }}
        >
          <Form.Item label="Họ và tên" name="fullName" rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <Form.Item label="Province/city" name="city" rules={[{ required: true }]}>
            <Select
              defaultValue={'---Choice Province/city---'}
              onChange={handleOnChangeProvince}
            >
              <Select.Option value = {""}>---Choice Province/city---</Select.Option>
              {listProvinces.map(p => (
                <Select.Option key={p["province_id"]} value={p["province_name"]}>{p["province_name"]}</Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item label="District" name="district" rules={[{ required: true }]}>
            <Select
              value={addressShipping.district}
              disabled = {listDistricts.length === 0}
              defaultValue={'---Choice District---'}
              onChange={handleOnChangeDistrict}
            >
              <Select.Option value = {""}>---Choice District---</Select.Option>
              {listDistricts.map(d => (
                <Select.Option key={d["district_id"]} value={d["district_name"]}>{d["district_name"]}</Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item label="Wards" name="wards" rules={[{ required: true }]}>
            <Select
              value={addressShipping.ward || '---Choice District---'}
              disabled = {listWards.length === 0}
              defaultValue={'---Choice Ward---'}
            >
              <Select.Option>---Choice Ward---</Select.Option>
              {listWards.map(w => (
                <Select.Option key={w["ward_id"]} value={w["ward_name"]}>{w["ward_name"]}</Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item label="Address" name="address" rules={[{ required: true }]}>
            <Input.TextArea />
          </Form.Item>

          <Form.Item label=" " name="address">
            <Checkbox >Checkbox</Checkbox>
            Set as default address
          </Form.Item>

          <Form.Item label=" " >
            <Button type="primary" htmlType="submit">
              Cập nhật
            </Button>
          </Form.Item>
        </Form>
      </Modal>     
    </div>
  )
}
