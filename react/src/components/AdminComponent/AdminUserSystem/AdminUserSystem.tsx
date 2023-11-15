import { EditOutlined } from '@ant-design/icons';
import { Button , Form, Input, Modal, Radio, Switch } from 'antd';
import { BiPlus } from 'react-icons/bi';
import TableComponent from '../../TableComponent/TableComponent';
import { useEffect, useState } from 'react';
import { createUserSystemService, deleteUserSystemService, editUserSystemService, getAllUserSystemService, getUserSystemService } from '../../../services/adminServices';
import { useMutation, useQuery } from '@tanstack/react-query';
import './AdminUserSystem.css'
import { convertDateAndTime, toastMSGObject } from '../../../utils/utils';
import { UserSystem } from '../../../model/UserModal';
import { toast } from 'react-toastify';
import { Action } from '../../../model/ActionModal';
import { RootState } from '../../../redux/store';
import { useSelector } from 'react-redux';


export default function AdminUserSystem() {

  const [form] = Form.useForm();
  const [isOpenModal , setisOpenModal] = useState(false);
  const [rowSelected , setRowSelected] = useState<any>({});
  const [typeAction , setTypeAction] = useState<Action>(Action.ADD);
  const user = useSelector((state: RootState) => state.user);

  // get all user sys
  const fetchGetAllUserSys = async () => {
    const res = await getAllUserSystemService();
    console.log(res)
    return res
  }

  const queryUserSys = useQuery(['all-user-sys'], fetchGetAllUserSys);
  const {data : listUserSyss , isLoading : isLoadingUserSyss} = queryUserSys

  // create UserSystem

  const mutationCreate = useMutation(
    (data : UserSystem) => createUserSystemService(data),
    {
      onSuccess : (res, variables) => {
        if(res?.status === 409){
          toast.error(res.message , toastMSGObject());
        }else{
          toast.success('Create successfully!' , toastMSGObject());
          handleCloseModal();
        }
      },
      onError : (error) => {
        console.log(error)
      },
      onSettled: () => {
        queryUserSys.refetch()
      }
    }
  )

  const handleCreateUserSystem = (valuesInput : UserSystem) => {
    mutationCreate.mutate(
      {...valuesInput , enabled : true}
    )
  }

  // update UserSystem

  const mutationUpdate = useMutation(
    (data : UserSystem) => editUserSystemService(data),
    {
      onSuccess : (res, variables) => {
        if(res?.status === 409){
          toast.error(res.message , toastMSGObject());
        }else{
          typeAction === Action.UPDATE && toast.success('Update successfully!' , toastMSGObject());
          handleCloseModal();
        }
      },
      onError : (error) => {
        console.log(error)
      },
      onSettled: () => {
        queryUserSys.refetch()
      }
    }
  )

  const handleUpdateUserSystem = (valuesInput : UserSystem) => {
    mutationUpdate.mutate(
      {...valuesInput , enabled : true}
    )
  }

  // soft delete UserSystem

  const mutationDisabled = useMutation(
    (data : string) => deleteUserSystemService(data),
    {
      onSuccess : () => {
        handleCloseModal();
      },
      onError : (error) => {
        console.log(error)
      },
      onSettled: () => {
        queryUserSys.refetch()
      }
    }
  )

  useEffect(() => {
    if(typeAction === Action.DELETE){
      if(!rowSelected.enabled){
        mutationUpdate.mutate(
          {
            username : rowSelected.username,
            role : rowSelected.role,
            enabled : true
          }
        )
      }else{
        mutationDisabled.mutate(rowSelected.username)
      }
    }else if(typeAction === Action.UPDATE){
      getUserSystemService(rowSelected.username)
        .then(res => form.setFieldsValue({
          ...res
        }))
    }
  },[rowSelected])

  // action modal
  const handleOpenModal = (typeAction : Action) => {
    setisOpenModal(true);
    setTypeAction(typeAction)
  }

  const handleCloseModal = () => {
    setisOpenModal(false);
    form.resetFields();
  }

  const renderAction = () => {
    return (
      <div>
        <EditOutlined 
          style={{ color: 'orange', fontSize: '30px', cursor: 'pointer' }} 
          onClick={() => handleOpenModal(Action.UPDATE)}
        />
      </div>
    )
  }

  const columns = [
    {
      title: 'Tên tài khoản',
      dataIndex: 'username',
      render: (text : string) => <span>{text}</span>,
      width : 200,
      sorter: (a : any,b : any) => a.username.length - b.username.length,
      isSearchProps : true
    },
    {
      title: 'Vai trò',
      dataIndex: 'role',
      render: (text : string) => <span>{text}</span>,
      filters: [
        { text: 'ROLE_SHOP_OWNER', value: 'ROLE_SHOP_OWNER' },
        { text: 'ROLE_ADMIN', value: 'ROLE_ADMIN' },
      ],
      onFilter: (value: string, record : any) => record.role.includes(value),
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdAt',
      render: (createdAt : string) => <span>{convertDateAndTime(createdAt).date}</span>,
      sorter: (a : any,b : any) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime(),
    },
    {
      title: 'Ngày chỉnh sửa',
      dataIndex: 'updatedAt',
      render: (updatedAt : string) => <span>{convertDateAndTime(updatedAt).date}</span>,
      sorter: (a : any,b : any) => new Date(a.updatedAt).getTime() - new Date(b.updatedAt).getTime(),
    },
    {
      title: 'Trạng thái',
      dataIndex: 'enabled',
      render: (isActive : boolean , record : any) => (
        <div style={
          {
            display:"flex" , 
            opacity: record.username === user.username ? 0.3 : 1,
            pointerEvents: record.username === user.username ? "none" : "auto"
          }
        }>
          <span style={{color : `${isActive ? 'rgb(0, 171, 86)' : 'red'}` , marginRight:20}}>⬤  {isActive ? 'Enable' : 'Disable'}</span>
          <Switch defaultChecked={isActive} onClick={() => setTypeAction(Action.DELETE)} />
        </div>
      ),
      filters: [
        { text: 'Enable', value: true },
        { text: 'Disable', value: false },
      ],
      onFilter: (value: boolean , record : any) => record.enabled === value,
    },
    {
      title: 'Action',
      dataIndex: 'action',
      render: renderAction
    }
  ];

  return (
    <div id='AdminUserSystem'>
      <div className="user-act-btn">
        <div className='total-user'>
          Tổng số lượng người dùng hệ thống : {listUserSyss?.length}
        </div>
        <Button type="primary" onClick={() => handleOpenModal(Action.ADD)}>
          <BiPlus/>
          Tạo người dùng mới
        </Button>
      </div>

      <TableComponent 
          columns={columns} 
          listData={listUserSyss} 
          isLoading={isLoadingUserSyss}
          onRow={(record : any, rowIndex : any) => {
              return {
                  onClick : (event : any) => {
                    setRowSelected(record);
                  }
              }
          }} 
          isRowSelection={false}                   
      />

      {/* <Modal 
        title="Delete User System" 
        open={typeAction === Action.DELETE && isOpenModal} 
        onCancel={handleCloseModal}
        footer = {[
          <Button type='primary' danger onClick={handleDisabledUserSystem}>
            <DeleteOutlined />
            Delete
          </Button>,
          <Button type='primary'>Cancel</Button>
        ]}
      >
        <p>Bạn muốn vô hiệu hóa chức năng của người dùng hệ thống này ?</p>
      </Modal> */}

      <Modal 
        width={400}
        title={`${typeAction} User System`} 
        open={typeAction !== Action.DELETE && isOpenModal} 
        onCancel={handleCloseModal}
        footer = {null}
      >
        <Form 
            style={{ width: '100%' }} 
            // layout="vertical"
            labelAlign="left" 
            autoComplete="off"
            form={form}
            colon={false} // mất dấu : ở label
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            onFinish={typeAction === Action.UPDATE ? handleUpdateUserSystem : handleCreateUserSystem}
        >
          <Form.Item
            label="Tên tài khoản"
            name="username"
            rules={[
              {
                required: true,
                message: `Vui lòng nhập tên tài khoản`
              }
            ]}
          >
            <Input disabled={typeAction === Action.UPDATE} placeholder="Tên tài khoản , ví dụ : LeKimTan" />
          </Form.Item>

          <Form.Item
            label={typeAction === Action.UPDATE ? "Mật khẩu mới" : "Mật khẩu"}
            name="password"
            rules={[
              {
                required: true,
                message: `Vui lòng nhập mật khẩu ${typeAction === Action.UPDATE ? 'mới' : ''}`
              },
              {
                min: 8,
                message: 'Mật khẩu phải từ 8 kí tự trở lên'
              }
            ]}  
          >
            <Input.Password placeholder="Mật khẩu phải từ 8 kí tự trở lên" />
          </Form.Item>

          <Form.Item
              label="Vai trò"
              name="role"
          >
            <Radio.Group>
              <Radio value={'ROLE_ADMIN'}>ADMIN</Radio>
              <Radio value={'ROLE_SHOP_OWNER'}>SHOP OWNER</Radio>
            </Radio.Group>
          </Form.Item>

          <Form.Item
              label=" "
          >
              <Button type="primary" htmlType="submit">
                {typeAction === Action.ADD ? 'Tạo mới' : 'Cập nhật'}
              </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}
