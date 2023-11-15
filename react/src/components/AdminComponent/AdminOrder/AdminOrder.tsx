import { useQuery } from "@tanstack/react-query";
import TableComponent from "../../TableComponent/TableComponent";
import { useMemo , useEffect, useState } from 'react';
import { getAllOrderService } from "../../../services/orderServices";
import { EditOutlined } from '@ant-design/icons';
import { convertDateAndTime } from "../../../utils/utils";
import { Button, Drawer, Empty, Steps } from "antd";
import LoadingComponent from "../../LoadingComponent/LoadingComponent";
import OrderDetailComponent from "../../OrderDetailComponent/OrderDetailComponent";
import './AdminOrder.css'
import { statusOrder } from "../../../utils/constants";

export default function AdminOrder() {
  const [rowSelected , setRowSelected] = useState<any>({});
  const [isOpenDrawer , setIsOpenDrawer] = useState(false);

  const fetchGetAllOrder = async () => {
    const res = await getAllOrderService();
    return res.data
  }

  const queryAllOrder = useQuery(['all-order'], fetchGetAllOrder);
  const {data : listAllOrders , isLoading : isLoadingAllOrders} = queryAllOrder
  console.log(listAllOrders)

  const items = useMemo(() => {
    return statusOrder.map(status => {
      if(status.key !== 'CANCELLED'){
        return {
          title : status.key
        }
      }
    })
  },[])
  
  const renderAction = () => {
    return (
      <div style={{ color: 'orange' , cursor: 'pointer' }}>
        <EditOutlined 
          style={{fontSize: '30px', marginRight:7}} 
          onClick={() => setIsOpenDrawer(true)}
        />
        Chi tiết
      </div>
    )
  }

  const columns = [
    {
      title: 'Id',
      dataIndex: 'orderId',
      render: (text : number) => <span>{text}</span>,
      width : 100,
    },
    {
      title: 'Tên người nhận',
      dataIndex: ['address','fullName'],
      render: (address : any) => {
        return (<span>{address.fullName}</span>)
      },
      width : 200,
      isSearchProps : true
    },
    {
      title: 'phương thức thanh toán',
      dataIndex: 'paymentMethod',
      render: (paymentMethod : string) => <span>{paymentMethod}</span>,
      sorter: (a : any,b : any) => a.paymentMethod - b.paymentMethod,
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      render: (status : string) => <span>{status}</span>,
      sorter: (a : any,b : any) => a.status - b.status,
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'grandTotal',
      render: (grandTotal : string) => <span>{grandTotal}</span>,
      sorter: (a : any,b : any) => a.grandTotal - b.grandTotal,
    },
    {
      title: 'Ngày tạo đơn',
      dataIndex: 'createdAt',
      render: (createdAt : string) => <span>{convertDateAndTime(createdAt).date}</span>,
      sorter: (a : any,b : any) => {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
      },
    },
    {
      title: 'Action',
      dataIndex: 'action',
      render: renderAction
    }
  ];

  return (
    <>
      <div id="AdminOrder">
        <div className="order-header">
          <div className="total-order">
            Tổng số lượng đơn hàng của hệ thống : 3
          </div>
        </div>

        <TableComponent 
            columns={columns} 
            listData={listAllOrders} 
            isLoading={isLoadingAllOrders}
            onRow={(record : any, rowIndex : any) => {
                return {
                    onClick : (event : any) => {
                      setRowSelected(record);
                    }
                }
            }} 
            isRowSelection={false}                   
        />
      </div>
      <Drawer
        title="Thông tin chi tiết đơn hàng"
        width={"80%"}
        onClose={() => setIsOpenDrawer(false)}
        open={isOpenDrawer}
      >
        {/* <LoadingComponent isloading={rowSelected?.orderId}>
          {rowSelected?.orderId ? (
            <OrderDetailComponent codeOrder={rowSelected?.orderId} />
          ) : (
            <Empty/>
          )}
        </LoadingComponent> */}
        <div className="order-detail-shopowner">
          <div className="status-order">
            <Steps
              size="small"
              direction="vertical"
              current={1}
              items={items && []}
            />
            <Button>Change Status</Button>
          </div>
          <OrderDetailComponent codeOrder={rowSelected?.orderId} />
        </div>
      </Drawer>    
    </>
  )
}
