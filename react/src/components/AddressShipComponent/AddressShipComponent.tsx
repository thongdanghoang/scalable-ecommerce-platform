import {AiOutlinePlus } from 'react-icons/ai'
import './Address.css'
import { Button, Input, Modal, Select , Form, Checkbox, Radio, Tooltip } from 'antd'
import { useEffect, useState } from 'react'
import { formatVietnamesePhone, getListDistricts, getListProvincesCity, getListWards, toastMSGObject} from '../../utils/utils';
import { getAddressShipsByUser , createAddressShip, updateAddressShip, deleteAddressShip } from '../../services/userService';
import { AddressShipping } from '../../model/UserModal';
import { useMutation, useQuery } from '@tanstack/react-query';
import { ToastContainer , toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AddressShipItem from './AddressShipItem';

export default function AddressShipComponent() {
  const [isModalOpen , setIsOpenModal] = useState(false);
  const [listProvinces , setListProvinces] = useState([]);
  const [listDistricts , setListDistricts] = useState([]); 
  const [listWards , setListWards] = useState([]); 
  const [form] = Form.useForm();
  const [isDisableCheckBox , setIsDisableCheckBox] = useState(false);
  const initialAddressShip = {
    id: 0,
    version: 0,
    fullName : '',
    phone : '',
    province : '',
    district : '',
    ward : '',
    addressDetail : '',
    type : '',
    default : false
  }
  const [addressShipping , setAddressShipping] = useState<AddressShipping>({
    ...initialAddressShip
  });
  const [isFormEdit , setIsFormEdit] = useState(false);

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
      province : nameCity + '-' + value.key,
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
      district : nameDistrict + '-' + value.key
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

  // handle get address ship by user

  const fetchGetAddressShipByUser = async () => {
    const res = await getAddressShipsByUser();
    return res.data
  }

  const queryAddressShip = useQuery({queryKey : ['addresses-ship-by-user-0'] , queryFn:fetchGetAddressShipByUser })
  const {data : listAddressShip} = queryAddressShip
  console.log(listAddressShip)

  const handleShowFormAddressShip = () => {
    setIsOpenModal(true);
    setAddressShipping({...initialAddressShip})
  }

  // handle create new address ship

  const mutationCreateAddress = useMutation(
    async (data : AddressShipping) => {
      const res = await createAddressShip(data)
      return res
    }
  )

  const {data : responseCreate , isSuccess : isSuccessCreate} = mutationCreateAddress;

  useEffect(() => {
    if(isSuccessCreate && responseCreate?.success){
      toast.success(responseCreate?.message, toastMSGObject())
      handleCancelModal();
    }else{
      toast.error(responseCreate?.message , toastMSGObject())
    }
  },[isSuccessCreate])

  const handleCreateAddressShip = () => {
    mutationCreateAddress.mutate({
      ...addressShipping,
      phone : `+84${addressShipping.phone}`
    } , {
      onSettled : () => {
        queryAddressShip.refetch();
      }
    })
  }

  // handle edit address ship

  const mutationEditAddress = useMutation(
    async (data : AddressShipping) => {
      const res = await updateAddressShip(data);
      return res
    }
  )

  const {data : responseUpdate , isSuccess : isSuccessUpdate} = mutationEditAddress;
  console.log(responseUpdate)

  useEffect(() => {
    if(isSuccessUpdate && responseUpdate?.success){
      toast.success(responseUpdate?.message,{
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      })
      handleCancelModal();
    }
  },[isSuccessUpdate])

  const handleEditAddressShip = () => {
    mutationEditAddress.mutate({
      ...addressShipping,
      phone : formatVietnamesePhone(addressShipping.phone)
    } , {
      onSettled : () => {
        queryAddressShip.refetch();
      }
    })
  }

  const handleShowDetailAddressShip = async (address : AddressShipping) => {
    setIsFormEdit(true);
    setIsOpenModal(true);
    const [province_name , province_id] = address.province.split('-');
    const [district_name , district_id] = address.district.split('-');
    form.setFieldsValue({
      ...address,
      phone : address.phone.replace('+84','0'),
      province : province_name,
      district : district_name
    })
    setIsDisableCheckBox(address.default)
    setAddressShipping({...address})
    setListDistricts(await getListDistricts(province_id))
    setListWards(await getListWards(district_id))
  }

  // handle Delete address ship

  const mutationDeleteAddress = useMutation(
    async (data : number) => await deleteAddressShip(data)
  )

  const handleDeleteAddressShip = async (idAddressShip : number) => {
    if(confirm('Sure Delete this address ship')){
      mutationDeleteAddress.mutate(idAddressShip, {
        onSettled : () => {
          queryAddressShip.refetch();
        }
      })
    }
  }

  const showTextToolTip = () => {
    return listAddressShip?.length === 0 ? `
      Địa chỉ đầu tiên của bạn được cài đặt làm Địa Chỉ Mặc Định. 
      Vui lòng thêm địa chỉ thứ hai để có thể thay đổi cài đặt này.
    ` : `
      Mỗi tài khoản phải có 1 địa chỉ mặc định.
    `
  }

  return (
    <div id='AddressShipComponent'>
      <ToastContainer/>
      <div className="add-address" onClick={handleShowFormAddressShip}>
        <AiOutlinePlus />
        <span>Add new address</span>
      </div>
      {listAddressShip && listAddressShip?.map((address : AddressShipping) => (
        <AddressShipItem 
          key={address.id}
          id={address.id}
          version={address.version}
          fullName={address.fullName}
          phone={address.phone}
          province={address.province}
          district={address.district}
          ward={address.ward}
          addressDetail={address.addressDetail}
          type={address.type}
          default={address.default}
          handleDeleteAddressShip={handleDeleteAddressShip}
          handleShowDetailAddressShip={handleShowDetailAddressShip}
        />
      ))}
      <Modal title={isFormEdit ? "Chỉnh sửa địa chỉ" : "Thêm địa chỉ mới"} open={isModalOpen} footer={null} onCancel={handleCancelModal}>
        <Form
          name="wrap"
          form={form}
          labelCol={{ flex: '130px' }}
          labelAlign="left"
          labelWrap
          wrapperCol={{ flex: 1 }}
          colon={false}
          style={{ maxWidth: 600 }}         
          onFinish={isFormEdit ? handleEditAddressShip : handleCreateAddressShip}
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
            {listAddressShip?.length === 0 || isDisableCheckBox ? (
              <Tooltip title={showTextToolTip()}>
                <Checkbox 
                  className={'disable-checkbox'}
                  disabled
                  name="default" 
                  onChange={handleOnChangeInput} 
                  checked={addressShipping.default}
                >
                  Set as default address
                </Checkbox>                
              </Tooltip>
            ) : (
              <Checkbox 
                name="default" 
                onChange={handleOnChangeInput} 
                checked={addressShipping.default}
              >
                Set as default address
              </Checkbox>
            )}
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
