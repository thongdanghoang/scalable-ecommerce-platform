import {AiOutlinePlus , AiOutlineCheckCircle , AiTwotoneEdit} from 'react-icons/ai'
import './Address.css'
import { Button, Input, Modal, Select , Form, Checkbox, Radio } from 'antd'
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
  const [form] = Form.useForm();
  const [addressShipping , setAddressShipping] = useState({
    fullName : '',
    phone : '',
    province : '',
    district : '',
    ward : '',
    addressDetail : '',
    type : '',
    default : ''
  })

  console.log(addressShipping)

  const handleCancelModal = () => {
    setIsOpenModal(false);
    form.resetFields();
    setListDistricts([]);
    setListWards([]);
  }

  useEffect(() => {
    const fetchProvincesCity = async () => {
      const data = await getListProvincesCity()
      setListProvinces(data)
    } 
    fetchProvincesCity();
  },[])

  const handleOnChangeInput = (e : any) => {
    setAddressShipping({
      ...addressShipping,
      [e.target.name] : e.target.name !== "default" ? e.target.value : e.target.checked
    })
  }

  const handleOnChangeProvince = async (nameCity : string, value : any) => {
    setAddressShipping({
      ...addressShipping ,
      province : nameCity,
      district : '',
      ward : ''
    })
    if(nameCity){
      setListDistricts([]);
      setListWards([]);
      form.setFieldsValue({
        district : "",
        ward : ""
      })
    }
    setListDistricts(await getListDistricts(value.key))
  }

  const handleOnChangeDistrict = async (nameDistrict : string, value : any) => {
    setAddressShipping({
      ...addressShipping ,
      district : nameDistrict
    })
    if(nameDistrict){
      setListWards([]);
      form.setFieldsValue({
        ward : ""
      })
    }
    setListWards(await getListWards(value.key))
  }

  const handleOnChangeWard = async (nameWard : string) => {
    setAddressShipping({
      ...addressShipping,
      ward : nameWard
    })
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
          form={form}
          labelCol={{ flex: '130px' }}
          labelAlign="left"
          labelWrap
          wrapperCol={{ flex: 1 }}
          colon={false}
          style={{ maxWidth: 600 }}
        >
          <Form.Item label="Họ và tên" name="fullName" rules={[{ required: true }]}>
            <Input placeholder='Nhập Họ và tên' name='fullName' value={addressShipping.fullName} onChange={handleOnChangeInput}/>
          </Form.Item>

          <Form.Item label="Số điện thoại" name="phone" rules={[{ required: true }]}>
            <Input placeholder='Nhập Số điện thoại' name='phone' value={addressShipping.phone} onChange={handleOnChangeInput}/>
          </Form.Item>

          <Form.Item label="Province/city" name="province" rules={[{ required: true }]}>
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
              //value={addressShipping.district}
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

          <Form.Item label="Wards" name="ward" rules={[{ required: true }]}>
            <Select
              disabled = {listWards.length === 0}
              defaultValue={'---Choice Ward---'}
              onChange={handleOnChangeWard}
            >
              <Select.Option value={""}>---Choice Ward---</Select.Option>
              {listWards.map(w => (
                <Select.Option key={w["ward_id"]} value={w["ward_name"]}>{w["ward_name"]}</Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item label="Address" name="addressDetail" rules={[{ required: true }]}>
            <Input.TextArea 
              placeholder='ví dụ : 52 Trần Hưng Đạo ...' 
              name='addressDetail'
              value={addressShipping.addressDetail}
              onChange={handleOnChangeInput}
            />
          </Form.Item>

          <Form.Item label="Loại địa chỉ" name="type">
            <Radio.Group name="type" onChange={handleOnChangeInput} value={addressShipping.type}>
              <Radio value={"HOME"}>HOME</Radio>
              <Radio value={"WORK"}>WORK</Radio>
              <Radio value={"OTHER"}>OTHER</Radio>
            </Radio.Group>
          </Form.Item>

          <Form.Item label=" " name="default">
            <Checkbox name="default" onChange={handleOnChangeInput} value={addressShipping.default}/>
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
