import { DeleteOutlined , EditOutlined } from '@ant-design/icons';
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


export default function AdminUserSystem() {

  const [form] = Form.useForm();
  const [isOpenModal , setisOpenModal] = useState(false);
  const [rowSelected , setRowSelected] = useState<any>({});
  const [typeAction , setTypeAction] = useState<Action>(Action.ADD);

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
          toast.success('Update successfully!' , toastMSGObject());
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

  const handleDisabledUserSystem = () => {
    console.log(rowSelected)
    // if(!rowSelected.role){
    //   mutationUpdate.mutate(
    //     {
    //       username : rowSelected.username,
    //       role : rowSelected.role,
    //       enabled : true
    //     }
    //   )
    // }else{
    //   mutationDisabled.mutate(rowSelected.username)
    // }
  }

  console.log(rowSelected)

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
        <DeleteOutlined
          style={{ color: 'red', fontSize: '30px', cursor: 'pointer' }} 
          onClick={() => handleOpenModal(Action.DELETE)}
        />
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
      sorter: (a : any,b : any) => a.name.length - b.name.length,
      isSearchProps : true
    },
    {
      title: 'Vai trò',
      dataIndex: 'role',
      render: (text : string) => <span>{text}</span>,
      sorter: (a : any,b : any) => a.sku.length - b.sku.length,
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdAt',
      render: (createdAt : string) => <span>{convertDateAndTime(createdAt).date}</span>,
      sorter: (a : any,b : any) => a.createdAt - b.createdAt,
    },
    {
      title: 'Ngày chỉnh sửa',
      dataIndex: 'updatedAt',
      render: (updatedAt : string) => <span>{convertDateAndTime(updatedAt).date}</span>,
      sorter: (a : any,b : any) => a.createdAt - b.createdAt,
    },
    {
      title: 'Trạng thái',
      dataIndex: 'enabled',
      render: (isActive : boolean) => (
        <div style={{display:"flex" , justifyContent:"space-around"}}>
          <span style={{color : `${isActive ? 'rgb(0, 171, 86)' : 'red'}`}}>⬤  {isActive ? 'Enable' : 'Disable'}</span>
          <Switch defaultChecked={isActive} onClick={handleDisabledUserSystem} />
        </div>
      ),
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
          Add new user system
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
                    getUserSystemService(record.username)
                      .then(res => form.setFieldsValue({
                        ...res
                      }))
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
        onCancel={() => setisOpenModal(false)}
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
              label="Username"
              name="username"
          >
            <Input placeholder="username , ex : LeKimTan" />
          </Form.Item>

          <Form.Item
              label={typeAction === Action.UPDATE ? "New password" : "Password"}
              name="password"
          >
            <Input.Password placeholder="" />
          </Form.Item>

          <Form.Item
              label="Role"
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
                {typeAction === Action.ADD ? 'Create' : 'Update'}
              </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}
