import { DeleteOutlined , EditOutlined } from '@ant-design/icons';
import { Button , Form, Modal } from 'antd';
import { BiPlus } from 'react-icons/bi';
import TableComponent from '../../TableComponent/TableComponent';
import { useState } from 'react';
import { getAllUserSystemService } from '../../../services/adminServices';
import { useQuery } from '@tanstack/react-query';
import './AdminUserSystem.css'


export default function AdminUserSystem() {

  const [form] = Form.useForm();
  const [isOpenModal , setisOpenModal] = useState(false) ;
  const [rowSelected , setRowSelected] = useState<any>({});

  // get all user sys
  const fetchGetAllUserSys = async () => {
    const res = await getAllUserSystemService();
    console.log(res)
    return res
  }

  const {data : listUserSyss , isLoading : isLoadingUserSyss} = useQuery(['all-user-sys'], fetchGetAllUserSys)

  const handleGetDetailUserSystem = () => {

  }

  const renderAction = () => {
    return (
    <div>
        <DeleteOutlined
          style={{ color: 'red', fontSize: '30px', cursor: 'pointer' }} 
          onClick={() => setisOpenModal(true)}
        />
        <EditOutlined 
          style={{ color: 'orange', fontSize: '30px', cursor: 'pointer' }} 
          onClick={handleGetDetailUserSystem}
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
      title: 'createdDate',
      dataIndex: 'createdAt',
      render: (text : string) => <span>{text}</span>,
      sorter: (a : any,b : any) => a.createdAt - b.createdAt,
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
        <Button type="primary" onClick={() => setisOpenModal(true)}>
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
                      setRowSelected(record)
                  }
              }
          }}                    
      />

      <Modal 
        title="Delete User System" 
        open={isOpenModal} 
        onCancel={() => setisOpenModal(false)}
        footer = {[
          <Button type='primary' danger>
            <DeleteOutlined />
            Delete
          </Button>,
          <Button type='primary'>Cancel</Button>
        ]}
      >
        <p>Bạn muốn xóa vĩnh viễn người dùng hệ thống này ?</p>
      </Modal>
    </div>
  )
}
